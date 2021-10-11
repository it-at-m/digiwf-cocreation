package io.miragon.bpmrepo.core.repository.domain.service;

import io.miragon.bpmrepo.core.repository.domain.mapper.AssignmentMapper;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.AssignmentJpaRepository;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.sharing.domain.mapper.SharedMapper;
import io.miragon.bpmrepo.core.sharing.infrastructure.entity.ShareWithRepositoryEntity;
import io.miragon.bpmrepo.core.sharing.infrastructure.repository.SharedRepositoryJpaRepository;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AssignmentJpaRepository repoAssignmentJpa;
    private final SharedRepositoryJpaRepository sharedRepositoryJpaRepository;
    private final SharedMapper sharedMapper;
    private final AssignmentMapper assignmentMapper;
    //private final TeamAssignmentJpaRepository teamAssignmentJpa;
    //private final RepoTeamAssignmentJpaRepository repoTeamAssignmentJpa;

    /**
     * This function checks the role of the user in a repository -> Roles that are required for creating or editing artifacts
     * First, it checks for direct assignments
     * TODO IF no direct assignment can be found, it fetches all Team assignments of the user and checks if one of the user's teams provides the rights
     */

    public void checkIfOperationIsAllowed(final String repositoryId, final RoleEnum minimumRequiredRole) {
        final String userId = this.userService.getUserIdOfCurrentUser();
        final AssignmentEntity assignmentEntity = this.repoAssignmentJpa.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(repositoryId, userId)
                .orElseThrow(() -> new AccessRightException("exception.authFailed"));

        //TODO use these Entites for the other checks
        //final List<TeamAssignmentEntity> teamAssignmentEntities = this.teamAssignmentJpa.findAllByTeamAssignmentId_UserId(userId);
        //final List<RepoTeamAssignmentEntity> repoTeamAssignmentEntities = this.repoTeamAssignmentJpa.findAllByRepoTeamAssignmentId_RepositoryId(repositoryId);


        //If two roles exist, compare them and use the higher one here
        final RoleEnum role = assignmentEntity.getRole();
        //0: OWNER - 1: ADMIN 2: MEMBER 3: VIEWER
        if (minimumRequiredRole.ordinal() >= role.ordinal()) {
            log.debug("AUTHORIZATION: ok");
        } else {
            throw new AccessRightException(
                    "authorization failed - Required role for this operation: \"" + minimumRequiredRole + "\" - Your role is: \"" + role.toString()
                            + "\"");
        }
    }


    /**
     * Pass the artifactId to also search for share-relations. Share-Relations should only enable the user to view data, not edit it -> use this method only for GET-Endpoints
     */
    public void checkIfOperationIsAllowed(final String repositoryId, final RoleEnum minimumRequiredRole, final String artifactId) {
        final String userId = this.userService.getUserIdOfCurrentUser();
        final Optional<AssignmentEntity> assignmentEntity = this.repoAssignmentJpa.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(repositoryId, userId);

        if (assignmentEntity.isPresent()) {
            final RoleEnum role = assignmentEntity.get().getRole();
            //0: OWNER - 1: ADMIN 2: MEMBER 3: VIEWER
            if (minimumRequiredRole.ordinal() >= role.ordinal()) {
                return;
            }
        }

        if (assignmentEntity.isEmpty()) {
            final List<ShareWithRepositoryEntity> sharedRepositories = this.sharedRepositoryJpaRepository.findByShareWithRepositoryId_ArtifactId(artifactId);
            if (sharedRepositories.size() == 0) {
                throw new AccessRightException("exception.noAssignmentOrShareFound");
            }
            if (sharedRepositories.size() > 0) {
                final List<AssignmentEntity> assignments = this.repoAssignmentJpa.findAssignmentEntitiesByAssignmentId_UserIdEquals(this.userService.getUserIdOfCurrentUser());
                final boolean shareRelationExists = this.loopThroughAssignments(sharedRepositories, assignments);
                if (!shareRelationExists) {
                    throw new AccessRightException("exception.noAssignmentOrShareFound");
                }
            }
        }
    }


    //returns true, if a share relation between user and one of the repositories the user can access exists, false if no relation to the artifact can be found
    private boolean loopThroughAssignments(final List<ShareWithRepositoryEntity> sharedRepositories, final List<AssignmentEntity> assignments) {
        for (final ShareWithRepositoryEntity sharedRepo : sharedRepositories) {
            for (final AssignmentEntity assignment : assignments) {
                if (sharedRepo.getShareWithRepositoryId().getRepositoryId().equals(assignment.getAssignmentId().getRepositoryId())) {
                    return true;
                }
            }
        }
        return false;
    }


    public void checkIfUserChangesOwnRole(final String targetUserId) {
        final String userId = this.userService.getUserIdOfCurrentUser();
        if (userId.equals(targetUserId)) {
            throw new AccessRightException("exception.changeOwnRole");
        }
    }
    

}
