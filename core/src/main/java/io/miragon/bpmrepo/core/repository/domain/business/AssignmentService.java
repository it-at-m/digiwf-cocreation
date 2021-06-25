package io.miragon.bpmrepo.core.repository.domain.business;

import io.miragon.bpmrepo.core.repository.api.transport.AssignmentTO;
import io.miragon.bpmrepo.core.repository.api.transport.AssignmentWithUserNameTO;
import io.miragon.bpmrepo.core.repository.domain.mapper.AssignmentMapper;
import io.miragon.bpmrepo.core.repository.domain.model.Assignment;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentId;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.AssignmentJpa;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
import io.miragon.bpmrepo.core.user.domain.mapper.UserMapper;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.infrastructure.entity.UserEntity;
import io.miragon.bpmrepo.core.user.infrastructure.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentJpa assignmentJpa;
    private final UserJpaRepository userJpa;
    private final AuthService authService;
    private final UserService userService;
    private final AssignmentMapper mapper;
    private final UserMapper userMapper;
    private final RepositoryService bpmnRepositoryService;

    public void createOrUpdateAssignment(final AssignmentWithUserNameTO assignmentWithUserNameTO) {
        this.authService.checkIfOperationIsAllowed(assignmentWithUserNameTO.getBpmnRepositoryId(), RoleEnum.ADMIN);
        final String newAssignmentUserId = this.userService.getUserIdByUsername(assignmentWithUserNameTO.getUserName());
        final String bpmnRepositoryId = assignmentWithUserNameTO.getBpmnRepositoryId();
        final String username = assignmentWithUserNameTO.getUserName();

        final RoleEnum newRole = assignmentWithUserNameTO.getRoleEnum();
        final AssignmentTO assignmentTO = new AssignmentTO(bpmnRepositoryId, newAssignmentUserId, username, newRole);
        final AssignmentEntity assignmentEntity = this.assignmentJpa
                .findByAssignmentId_BpmnRepositoryIdAndAssignmentId_UserId(assignmentTO.getBpmnRepositoryId(), newAssignmentUserId);
        if (assignmentEntity == null) {
            this.createAssignment(assignmentTO);
            log.debug("Created assignment");
        } else {
            this.updateAssignment(assignmentTO);
            log.debug(String.format("Updated role to %s", assignmentTO.getRoleEnum().toString()));
        }
    }

    public void createAssignment(final AssignmentTO assignmentTO) {
        final Assignment assignment = this.mapper.toModel(assignmentTO);
        final AssignmentId assignmentId = this.mapper.toEmbeddable(assignment.getUserId(), assignment.getBpmnRepositoryId());
        final AssignmentEntity assignmentEntity = this.mapper.toEntity(assignment, assignmentId);
        this.saveToDb(assignmentEntity);
        final Integer assignedUsers = this.assignmentJpa.countByAssignmentId_BpmnRepositoryId(assignment.getBpmnRepositoryId());
        this.bpmnRepositoryService.updateAssignedUsers(assignment.getBpmnRepositoryId(), assignedUsers);
    }

    //0: Owner, 1: Admin, 2: Member, 3: Viewer
    public void updateAssignment(final AssignmentTO assignmentTO) {
        //Assignments can be managed by Admins and Owners
        final String newAssignmentUserId = assignmentTO.getUserId();
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        this.authService.checkIfOperationIsAllowed(assignmentTO.getBpmnRepositoryId(), RoleEnum.ADMIN);

        final RoleEnum currentUserRole = this.getUserRole(assignmentTO.getBpmnRepositoryId(), currentUserId);
        final RoleEnum affectedUserRole = this.getUserRole(assignmentTO.getBpmnRepositoryId(), newAssignmentUserId);

        //Exception if the user tries to change its own rights
        if (newAssignmentUserId == currentUserId) {
            throw new AccessRightException("You can't change your own role");
        }
        //Exception if the user tries to change the role of someone with higher permissions (if an admin tries to change the role of an owner)
        if (affectedUserRole.ordinal() < currentUserRole.ordinal()) {
            throw new AccessRightException(String.format("You cant change the role of %s because your role provides less rights (You are an \"%s\")",
                    this.getUserRole(assignmentTO.getBpmnRepositoryId(), newAssignmentUserId),
                    this.getUserRole(assignmentTO.getBpmnRepositoryId(), this.userService.getUserIdOfCurrentUser())));
        }
        //Exception if the user tries to give someone a role that is higher than its own
        if (currentUserRole.ordinal() > assignmentTO.getRoleEnum().ordinal()) {
            throw new AccessRightException("You can't assign roles with higher permissions than your own");
        }
        final Assignment assignment = new Assignment(assignmentTO);
        final AssignmentId assignmentId = this.mapper.toEmbeddable(assignment.getUserId(), assignment.getBpmnRepositoryId());
        final AssignmentEntity assignmentEntity = this.mapper.toEntity(assignment, assignmentId);
        this.saveToDb(assignmentEntity);
    }

    public void createInitialAssignment(final String bpmnRepositoryId) {
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final UserEntity currentUserEntity = this.userJpa.findByUserId(currentUserId);
        final User currentUser = this.userMapper.toModel(currentUserEntity);
        final String currentUserName = currentUser.getUserName();
        final AssignmentTO assignmentTO = new AssignmentTO(bpmnRepositoryId, currentUserId, currentUserName, RoleEnum.OWNER);
        final Assignment assignment = new Assignment(assignmentTO);
        final AssignmentEntity assignmentEntity = this.mapper.toEntity(assignment, this.mapper.toEmbeddable(currentUserId, bpmnRepositoryId));
        this.assignmentJpa.save(assignmentEntity);
    }

    //receive all AssignmentEntities related to the user
    public List<String> getAllAssignedRepositoryIds(final String userId) {
        return this.assignmentJpa.findAssignmentEntitiesByAssignmentId_UserIdEquals(userId).stream()
                .map(assignmentEntity -> assignmentEntity.getAssignmentId().getBpmnRepositoryId())
                .collect(Collectors.toList());
    }

    public AssignmentEntity getAssignmentEntity(final String bpmnRepositoryId, final String userId) {
        return this.assignmentJpa.findByAssignmentId_BpmnRepositoryIdAndAssignmentId_UserId(bpmnRepositoryId, userId);
    }

    public RoleEnum getUserRole(final String bpmnRepositoryId, final String userId) {
        final AssignmentEntity assignmentEntity = this.getAssignmentEntity(bpmnRepositoryId, userId);
        return assignmentEntity.getRoleEnum();
    }

    public List<AssignmentTO> getAllAssignedUsers(final String bpmnRepositoryId) {
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.MEMBER);
        final List<AssignmentEntity> assignments = this.assignmentJpa.findByAssignmentId_BpmnRepositoryId(bpmnRepositoryId);
        final List<AssignmentTO> assignedUsers = assignments.stream()
                .map(assignmentEntity -> this.mapper.toTO(this.mapper.toModel(assignmentEntity)))
                .collect(Collectors.toList());
        return assignedUsers;
    }

    public void saveToDb(final AssignmentEntity assignmentEntity) {
        this.authService.checkIfOperationIsAllowed(assignmentEntity.getAssignmentId().getBpmnRepositoryId(), RoleEnum.ADMIN);
        this.assignmentJpa.save(assignmentEntity);
    }

    public void deleteAssignment(final String bpmnRepositoryId, final String deletedUsername) {
        final String deletedUserId = this.userService.getUserIdByUsername(deletedUsername);
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.ADMIN);

        final RoleEnum currentUserRole = this.getUserRole(bpmnRepositoryId, currentUserId);
        final RoleEnum deletedUserRole = this.getUserRole(bpmnRepositoryId, deletedUserId);
        //role of deleted user has to be equal or weaker than role of current user (0:Owner, 1:Admin, 2:Member, 3: Viewer)
        if (currentUserRole.ordinal() > deletedUserRole.ordinal()) {
            throw new AccessRightException(
                    String.format("You cant remove %s (Repository-%s) from this repository because your role provides less rights (You are an %s)",
                            deletedUsername,
                            deletedUserRole,
                            currentUserRole));
        }
        this.assignmentJpa.deleteAssignmentEntityByAssignmentId_BpmnRepositoryIdAndAssignmentId_UserId(bpmnRepositoryId, deletedUserId);
        final Integer assignedUsers = this.assignmentJpa.countByAssignmentId_BpmnRepositoryId(bpmnRepositoryId);
        this.bpmnRepositoryService.updateAssignedUsers(bpmnRepositoryId, assignedUsers);
    }

    public void deleteAllByRepositoryId(final String bpmnRepositoryId) {
        //Auth check in RepositoryFacade
        //Is only called if the corresponding repository is deleted
        final int deletedAssignments = this.assignmentJpa.deleteAllByAssignmentId_BpmnRepositoryId(bpmnRepositoryId);
        log.debug(String.format("Deleted Assignments for all %s users", deletedAssignments));
    }
}
