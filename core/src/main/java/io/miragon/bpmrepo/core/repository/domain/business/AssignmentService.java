package io.miragon.bpmrepo.core.repository.domain.business;

import io.miragon.bpmrepo.core.repository.domain.mapper.AssignmentMapper;
import io.miragon.bpmrepo.core.repository.domain.model.Assignment;
import io.miragon.bpmrepo.core.repository.domain.model.AssignmentUpdate;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.AssignmentJpaRepository;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
import io.miragon.bpmrepo.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentJpaRepository assignmentJpaRepository;
    private final AuthService authService;
    private final UserService userService;
    private final AssignmentMapper mapper;
    private final RepositoryService repositoryService;

    public void createOrUpdateAssignment(final AssignmentUpdate assignmentUpdate) {
        this.authService.checkIfOperationIsAllowed(assignmentUpdate.getRepositoryId(), RoleEnum.ADMIN);
        final Assignment assignment = new Assignment(assignmentUpdate);

        final String newAssignmentUserId = this.userService.getUserIdByUsername(assignmentUpdate.getUsername());
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final RoleEnum currentUserRole = this.getUserRole(assignment.getRepositoryId(), currentUserId);

        final Optional<AssignmentEntity> assignmentEntity = this.assignmentJpaRepository
                .findByAssignmentId_RepositoryIdAndAssignmentId_UserId(assignmentUpdate.getRepositoryId(), newAssignmentUserId);

        //Exception if the user tries to give someone a role that is higher than its own
        if (currentUserRole.ordinal() > assignment.getRoleEnum().ordinal()) {
            throw new AccessRightException("You can't assign roles with higher permissions than your own");
        }

        //Exception if the user tries to change its own rights
        if (assignment.getUserId().equals(currentUserId)) {
            throw new AccessRightException("You can't change your own role");
        }

        if (assignmentEntity.isEmpty()) {
            this.createAssignment(assignment);
            log.debug("Created assignment");
        } else {
            this.updateAssignment(assignment);
            log.debug(String.format("Updated role to %s", assignment.getRoleEnum().toString()));
        }
    }

    public void createInitialAssignment(final String repositoryId) {
        final User currentUser = this.userService.getCurrentUser();

        final Assignment assignment = Assignment.builder()
                .repositoryId(repositoryId)
                .roleEnum(RoleEnum.OWNER)
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .build();

        this.saveToDb(assignment);
    }

    //receive all AssignmentEntities related to the user
    public List<String> getAllAssignedRepositoryIds(final String userId) {
        return this.assignmentJpaRepository.findAssignmentEntitiesByAssignmentId_UserIdEquals(userId).stream()
                .map(assignmentEntity -> assignmentEntity.getAssignmentId().getRepositoryId())
                .collect(Collectors.toList());
    }

    public AssignmentEntity getAssignmentEntity(final String bpmnRepositoryId, final String userId) {
        return this.assignmentJpaRepository.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(bpmnRepositoryId, userId)
                .orElseThrow();
    }

    public RoleEnum getUserRole(final String bpmnRepositoryId, final String userId) {
        final AssignmentEntity assignmentEntity = this.getAssignmentEntity(bpmnRepositoryId, userId);
        return assignmentEntity.getRoleEnum();
    }

    public List<Assignment> getAllAssignedUsers(final String repositoryId) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        final List<AssignmentEntity> assignments = this.assignmentJpaRepository.findByAssignmentId_RepositoryId(repositoryId);
        return this.mapper.mapToModel(assignments);
    }

    public void deleteAssignment(final String repositoryId, final String deletedUsername) {
        final String deletedUserId = this.userService.getUserIdByUsername(deletedUsername);
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.ADMIN);

        final RoleEnum currentUserRole = this.getUserRole(repositoryId, currentUserId);
        final RoleEnum deletedUserRole = this.getUserRole(repositoryId, deletedUserId);
        //role of deleted user has to be equal or weaker than role of current user (0:Owner, 1:Admin, 2:Member, 3: Viewer)
        if (currentUserRole.ordinal() > deletedUserRole.ordinal()) {
            throw new AccessRightException(
                    String.format("You cant remove %s (Repository-%s) from this repository because your role provides less rights (You are an %s)",
                            deletedUsername,
                            deletedUserRole,
                            currentUserRole));
        }

        this.assignmentJpaRepository.deleteAssignmentEntityByAssignmentId_RepositoryIdAndAssignmentId_UserId(repositoryId, deletedUserId);
        final Integer assignedUsers = this.assignmentJpaRepository.countByAssignmentId_RepositoryId(repositoryId);
        this.repositoryService.updateAssignedUsers(repositoryId, assignedUsers);
    }

    public void deleteAllByRepositoryId(final String bpmnRepositoryId) {
        //Auth check in RepositoryFacade
        //Is only called if the corresponding repository is deleted
        final int deletedAssignments = this.assignmentJpaRepository.deleteAllByAssignmentId_RepositoryId(bpmnRepositoryId);
        log.debug(String.format("Deleted Assignments for all %s users", deletedAssignments));
    }

    // helper methods

    private void updateAssignment(final Assignment assignment) {
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final RoleEnum currentUserRole = this.getUserRole(assignment.getRepositoryId(), currentUserId);
        final RoleEnum affectedUserRole = this.getUserRole(assignment.getRepositoryId(), assignment.getUserId());

        //Exception if the user tries to change the role of someone with higher permissions (if an admin tries to change the role of an owner)
        if (affectedUserRole.ordinal() < currentUserRole.ordinal()) {
            throw new AccessRightException(String.format("You cant change the role of %s because your role provides less rights (You are an \"%s\")",
                    this.getUserRole(assignment.getRepositoryId(), assignment.getUserId()),
                    this.getUserRole(assignment.getRepositoryId(), this.userService.getUserIdOfCurrentUser())));
        }
        this.saveToDb(assignment);
    }

    private void createAssignment(final Assignment assignment) {
        this.saveToDb(assignment);
        final Integer assignedUsers = this.assignmentJpaRepository.countByAssignmentId_RepositoryId(assignment.getRepositoryId());
        this.repositoryService.updateAssignedUsers(assignment.getRepositoryId(), assignedUsers);
    }

    private Assignment saveToDb(final Assignment assignment) {
        final AssignmentEntity savedAssignment = this.assignmentJpaRepository
                .save(this.mapper.mapToEntity(assignment, this.mapper.mapToEmbeddable(assignment.getUserId(), assignment.getRepositoryId())));
        return this.mapper.mapToModel(savedAssignment);
    }
}
