package io.miragon.bpmrepo.core.team.domain.facade;

import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.team.api.transport.TeamAssignmentTO;
import io.miragon.bpmrepo.core.team.domain.mapper.TeamAssignmentMapper;
import io.miragon.bpmrepo.core.team.domain.model.TeamAssignment;
import io.miragon.bpmrepo.core.team.domain.service.TeamAssignmentService;
import io.miragon.bpmrepo.core.team.domain.service.TeamAuthService;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamAssignmentFacade {
    private final TeamAssignmentMapper mapper;
    private final TeamAuthService teamAuthService;
    private final AuthService authService;
    private final UserService userService;
    private final TeamAssignmentService teamAssignmentService;

    public TeamAssignment createTeamAssignment(final TeamAssignmentTO teamAssignmentTO) {
        log.debug("Creating Assignment for Team {}", teamAssignmentTO.getTeamId());
        this.teamAuthService.checkIfTeamOperationIsAllowed(teamAssignmentTO.getTeamId(), RoleEnum.ADMIN);
        this.teamAuthService.checkIfAssignedRoleIsLowerThanOwnRole(teamAssignmentTO.getTeamId(), teamAssignmentTO.getRole());

        final TeamAssignment teamAssignment = this.mapper.mapToModel(teamAssignmentTO);
        return this.teamAssignmentService.createTeamAssignment(teamAssignment);
    }

    public TeamAssignment createInitialTeamAssignment(final String teamId, final User user) {
        log.debug("Setting user {} as OWNER of newly created Team {}", user.getUsername(), teamId);
        final TeamAssignment teamAssignment = new TeamAssignment(user.getId(), user.getUsername(), teamId, RoleEnum.OWNER);
        return this.teamAssignmentService.createInitialTeamAssignment(teamAssignment);
    }

    public List<TeamAssignment> getAllAssignments(final String userId) {
        return this.teamAssignmentService.getAllAssignments(userId);
    }

/*    public List<TeamAssignment> getAllTeamAssignments(final String teamId) {
        return this.teamAssignmentService.getAlLTeamAssignments(teamId);
    }*/

    public TeamAssignment updateTeamAssignment(final TeamAssignmentTO teamAssignmentTO) {
        log.debug("Checking permissions");
        this.teamAuthService.checkIfTeamOperationIsAllowed(teamAssignmentTO.getTeamId(), RoleEnum.ADMIN);
        this.authService.checkIfUserChangesOwnRole(teamAssignmentTO.getUserId());
        final TeamAssignment teamAssignment = this.mapper.mapToModel(teamAssignmentTO);
        return this.teamAssignmentService.updateTeamAssignment(teamAssignment);
    }


    public List<TeamAssignment> getAllAssignedUsers(final String teamId) {
        log.debug("Checking permissions");
        this.teamAuthService.checkIfTeamOperationIsAllowed(teamId, RoleEnum.VIEWER);
        return this.teamAssignmentService.getAllAssignedUsers(teamId);

    }

    public void deleteAssignment(final String teamId, final String userId) {
        log.debug("Checking permissions");
        this.teamAuthService.checkIfTeamOperationIsAllowed(teamId, RoleEnum.ADMIN);
        //TODO: Role of user must be lower than role of deleted user
        this.teamAssignmentService.deleteAssignment(teamId, userId);
    }
}
