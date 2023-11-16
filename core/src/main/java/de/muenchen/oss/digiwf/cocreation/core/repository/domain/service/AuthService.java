package de.muenchen.oss.digiwf.cocreation.core.repository.domain.service;

import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.AssignmentEntity;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.repository.AssignmentJpaRepository;
import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.AccessRightException;
import de.muenchen.oss.digiwf.cocreation.core.sharing.infrastructure.entity.ShareWithRepositoryEntity;
import de.muenchen.oss.digiwf.cocreation.core.sharing.infrastructure.repository.SharedRepositoryJpaRepository;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.service.UserService;
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

    /**
     * This function checks the role of the user in a repository. Roles that are required for creating or editing artifacts
     * First, it checks for direct assignments
     *
     * @param repositoryId
     * @param minimumRequiredRole
     */
    public void checkIfOperationIsAllowed(final String repositoryId, final RoleEnum minimumRequiredRole) {
        final String userId = this.userService.getCurrentUser().getId();
        final AssignmentEntity assignmentEntity = this.repoAssignmentJpa.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(repositoryId, userId)
                .orElseThrow(() -> new AccessRightException("exception.authFailed"));

        //If two roles exist, compare them and use the higher one here
        final RoleEnum role = assignmentEntity.getRole();
        //0: OWNER - 1: ADMIN 2: MEMBER 3: VIEWER
        if (minimumRequiredRole.ordinal() < role.ordinal()) {
            throw new AccessRightException("authorization failed - Required role for this operation: \"" + minimumRequiredRole + "\" - Your role is: \"" + role + "\"");
        }
    }


    /**
     * Pass the artifactId to also search for share-relations.
     * Share-Relations should only enable the user to view data, not edit it use this method only for GET-Endpoints
     *
     * @param repositoryId
     * @param minimumRequiredRole
     * @param artifactId
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

    /**
     * Returns true, if a share relation between user and one of the repositories the user can access exists.
     * False if no relation to the artifact can be found.
     *
     * @param sharedRepositories
     * @param assignments
     * @return
     */
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
