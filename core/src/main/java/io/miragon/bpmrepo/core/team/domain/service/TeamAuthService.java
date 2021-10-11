package io.miragon.bpmrepo.core.team.domain.service;


import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamAssignmentEntity;
import io.miragon.bpmrepo.core.team.infrastructure.repository.TeamAssignmentJpaRepository;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamAuthService {

    private final UserService userService;
    private final TeamAssignmentJpaRepository teamAssignmentJpa;

    /**
     * This function checks the role of the user inside a team -> Roles that are required for adding members to the team or editing the team's information
     */

    public void checkIfTeamOperationIsAllowed(final String teamId, final RoleEnum minimumRequiredRole) {
        final String userId = this.userService.getUserIdOfCurrentUser();
        final TeamAssignmentEntity teamAssignmentEntity = this.teamAssignmentJpa.findByTeamAssignmentId_TeamIdAndTeamAssignmentId_UserId(teamId, userId)
                .orElseThrow(() -> new AccessRightException("authorization failed - You are not assigned to this Team"));

        final RoleEnum role = teamAssignmentEntity.getRole();
        //0: OWNER - 1: ADMIN 2: MEMBER 3: VIEWER
        if (minimumRequiredRole.ordinal() >= role.ordinal()) {
            log.debug("AUTHORIZATION: ok");
        } else {
            throw new AccessRightException(
                    "authorization failed - Required role for this operation: \"" + minimumRequiredRole + "\" - Your role is: \"" + role.toString()
                            + "\"");
        }
    }

    public void checkIfAssignedRoleIsLowerThanOwnRole(final String teamId, final RoleEnum givenRole) {
        final String userId = this.userService.getUserIdOfCurrentUser();
        final TeamAssignmentEntity teamAssignmentEntity = this.teamAssignmentJpa.findByTeamAssignmentId_TeamIdAndTeamAssignmentId_UserId(teamId, userId)
                .orElseThrow(() -> new AccessRightException("authorization failed - You are not assigned to this Team"));

        final RoleEnum role = teamAssignmentEntity.getRole();
        //0: OWNER - 1: ADMIN 2: MEMBER 3: VIEWER
        if (givenRole.ordinal() >= role.ordinal()) {
            log.debug("AUTHORIZATION: ok");
        } else {
            throw new AccessRightException(
                    "authorization failed - Your role provides less rights than \"" + givenRole + "\" - Your role is: \"" + role.toString()
                            + "\"");
        }
    }
}
