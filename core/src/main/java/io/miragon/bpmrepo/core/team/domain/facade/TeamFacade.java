package io.miragon.bpmrepo.core.team.domain.facade;


import io.miragon.bpmrepo.core.team.domain.model.NewTeam;
import io.miragon.bpmrepo.core.team.domain.model.Team;
import io.miragon.bpmrepo.core.team.domain.service.TeamService;
import io.miragon.bpmrepo.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamFacade {

    private final TeamService teamService;
    private final TeamAssignmentFacade teamAssignmentFacade;

    public Team createTeam(final NewTeam newTeam, final User user) {
        log.debug("Checking if Name is available");
        final Team team = this.teamService.createTeam(newTeam);

        this.teamAssignmentFacade.createInitialTeamAssignment(team.getId(), user);
        return team;
    }
}
