package io.miragon.bpmrepo.core.team.domain.facade;


import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.team.domain.model.NewTeam;
import io.miragon.bpmrepo.core.team.domain.model.Team;
import io.miragon.bpmrepo.core.team.domain.model.TeamAssignment;
import io.miragon.bpmrepo.core.team.domain.model.TeamUpdate;
import io.miragon.bpmrepo.core.team.domain.service.TeamAuthService;
import io.miragon.bpmrepo.core.team.domain.service.TeamService;
import io.miragon.bpmrepo.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamFacade {

    private final TeamService teamService;
    private final TeamAssignmentFacade teamAssignmentFacade;
    private final TeamAuthService teamAuthService;

    public Team createTeam(final NewTeam newTeam, final User user) {
        log.debug("Checking if Name is available");
        final Team team = this.teamService.createTeam(newTeam);

        this.teamAssignmentFacade.createInitialTeamAssignment(team.getId(), user);
        return team;
    }

    public Team updateTeam(final String teamId, final TeamUpdate teamUpdate) {
        log.debug("Checking Permission");
        this.teamAuthService.checkIfTeamOperationIsAllowed(teamId, RoleEnum.ADMIN);
        return this.teamService.updateTeam(teamId, teamUpdate);
    }

    public List<Team> getAllTeams(final String currentUserId) {
        log.debug("Checking Assignments");
        final List<TeamAssignment> teamAssignments = this.teamAssignmentFacade.getAllAssignments(currentUserId);
        final List<String> teamIds = teamAssignments.stream().map(TeamAssignment::getTeamId).collect(Collectors.toList());
        return this.teamService.getAlLTeams(teamIds);
    }

    public Team getTeam(final String teamId) {
        log.debug("Checking Permissions");
        this.teamAuthService.checkIfTeamOperationIsAllowed(teamId, RoleEnum.VIEWER);
        return this.teamService.getTeam(teamId);
    }

    public List<Team> searchTeams(final String typedName) {
        return this.teamService.searchTeams(typedName);
    }

    public void deleteTeam(final String teamId) {
        log.debug("Checking Permission");
        this.teamAuthService.checkIfTeamOperationIsAllowed(teamId, RoleEnum.OWNER);
        this.teamService.deleteTeam(teamId);
    }
}
