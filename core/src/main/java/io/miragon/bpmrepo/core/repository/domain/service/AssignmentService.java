package io.miragon.bpmrepo.core.repository.domain.service;

import io.miragon.bpmrepo.core.repository.domain.mapper.AssignmentMapper;
import io.miragon.bpmrepo.core.repository.domain.model.Assignment;
import io.miragon.bpmrepo.core.repository.domain.model.AssignmentUpdate;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.AssignmentJpaRepository;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    public Assignment updateAssignment(final AssignmentUpdate assignmentUpdate) {
        log.debug("Persisting assignment update");
        this.authService.checkIfOperationIsAllowed(assignmentUpdate.getRepositoryId(), RoleEnum.ADMIN);
        final Assignment assignment = new Assignment(assignmentUpdate);

        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final RoleEnum currentUserRole = this.getUserRole(assignment.getRepositoryId(), currentUserId);
        final RoleEnum affectedUserRole = this.getUserRole(assignment.getRepositoryId(), assignment.getUserId());

        //Exception if the user tries to change the role of someone with higher permissions (if an admin tries to change the role of an owner)
        if (affectedUserRole.ordinal() < currentUserRole.ordinal()) {
            throw new AccessRightException(String.format("You cant change the role of %s because your role provides less rights (You are an \"%s\")",
                    this.getUserRole(assignment.getRepositoryId(), assignment.getUserId()),
                    this.getUserRole(assignment.getRepositoryId(), this.userService.getUserIdOfCurrentUser())));
        }
        return this.saveToDb(assignment);
    }


    public Assignment createAssignment(final AssignmentUpdate assignmentUpdate) {
        log.debug("Persisting new assignment");
        this.authService.checkIfOperationIsAllowed(assignmentUpdate.getRepositoryId(), RoleEnum.ADMIN);
        final Assignment assignment = new Assignment(assignmentUpdate);

        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final RoleEnum currentUserRole = this.getUserRole(assignment.getRepositoryId(), currentUserId);

        if (currentUserRole.ordinal() > assignment.getRole().ordinal()) {
            throw new AccessRightException("You can't assign roles with higher permissions than your own");
        }
        if (assignment.getUserId().equals(currentUserId)) {
            throw new AccessRightException("You can't change your own role");
        }
        final Assignment createdAssignment = this.saveToDb(assignment);
        final Integer assignedUsers = this.assignmentJpaRepository.countByAssignmentId_RepositoryId(assignment.getRepositoryId());
        this.repositoryService.updateAssignedUsers(assignment.getRepositoryId(), assignedUsers);
        return createdAssignment;
    }


    public void createInitialAssignment(final String repositoryId) {
        log.debug("Persisting initial assignment");
        final User currentUser = this.userService.getCurrentUser();

        final Assignment assignment = Assignment.builder()
                .repositoryId(repositoryId)
                .role(RoleEnum.OWNER)
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .build();

        this.saveToDb(assignment);
    }

    //receive all AssignmentEntities related to the user
    public List<String> getAllAssignedRepositoryIds(final String userId) {
        log.debug("Querying assignments");
        final Optional<List<Assignment>> assignments = this.assignmentJpaRepository.findAssignmentEntitiesByAssignmentId_UserIdEquals(userId).map(this.mapper::mapToModel);
        if (assignments.isEmpty()) {
            throw new ObjectNotFoundException();
        }
        return assignments.get().stream().map(Assignment::getRepositoryId).collect(Collectors.toList());
    }

    public List<String> getManageableRepositoryIds(final String userId) {
        log.debug("Querying ADMIN and OWNER assignments");
        final List<RoleEnum> roles = new ArrayList<>();
        roles.add(RoleEnum.ADMIN);
        roles.add(RoleEnum.OWNER);
        final Optional<List<Assignment>> assignments = this.assignmentJpaRepository.findByAssignmentId_UserIdAndRoleIn(userId, roles).map(this.mapper::mapToModel);
        if (assignments.isEmpty()) {
            throw new ObjectNotFoundException();
        }
        return assignments.get().stream().map(Assignment::getRepositoryId).collect(Collectors.toList());
    }

    public AssignmentEntity getAssignmentEntity(final String repositoryId, final String userId) {
        log.debug("Querying assignment");
        return this.assignmentJpaRepository.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(repositoryId, userId)
                .orElseThrow();
    }

    public RoleEnum getUserRole(final String repositoryId, final String userId) {
        final AssignmentEntity assignmentEntity = this.getAssignmentEntity(repositoryId, userId);
        return assignmentEntity.getRole();
    }

    public List<Assignment> getAllAssignedUsers(final String repositoryId) {
        log.debug("Querying all assigned users");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        final List<AssignmentEntity> assignments = this.assignmentJpaRepository.findByAssignmentId_RepositoryId(repositoryId);
        return this.mapper.mapToModel(assignments);
    }

    public void deleteAssignment(final String repositoryId, final String deletedUsername) {
        log.debug("Deleting assignment");
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

    public void deleteAllByRepositoryId(final String repositoryId) {
        final int deletedAssignments = this.assignmentJpaRepository.deleteAllByAssignmentId_RepositoryId(repositoryId);
        log.debug("Deleted Assignments for all {} users", deletedAssignments);
    }


    private Assignment saveToDb(final Assignment assignment) {
        final AssignmentEntity savedAssignment = this.assignmentJpaRepository
                .save(this.mapper.mapToEntity(assignment, this.mapper.mapToEmbeddable(assignment.getUserId(), assignment.getRepositoryId())));
        return this.mapper.mapToModel(savedAssignment);
    }
}
