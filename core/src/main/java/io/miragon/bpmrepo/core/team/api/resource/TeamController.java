package io.miragon.bpmrepo.core.team.api.resource;


import io.miragon.bpmrepo.core.team.api.mapper.TeamApiMapper;
import io.miragon.bpmrepo.core.team.api.transport.NewTeamTO;
import io.miragon.bpmrepo.core.team.api.transport.TeamTO;
import io.miragon.bpmrepo.core.team.domain.facade.TeamFacade;
import io.miragon.bpmrepo.core.team.domain.model.Team;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Slf4j
@Validated
@Transactional
@RestController
@RequiredArgsConstructor
@Tag(name = "Team")
@RequestMapping("/api/team")
public class TeamController {

    private final TeamFacade teamFacade;
    private final UserService userService;
    private final TeamApiMapper apiMapper;


    /**
     * Create a new Team
     *
     * @param newTeamTO team that should be created
     * @return the created team
     */
    @Operation(summary = "Create a new Team")
    @PostMapping()
    public ResponseEntity<TeamTO> createTeam(@RequestBody @Valid final NewTeamTO newTeamTO) {
        log.debug("Creating new Team");
        final Team team = this.teamFacade.createTeam(this.apiMapper.mapToModel(newTeamTO), this.userService.getCurrentUser());
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(team));
    }


}
