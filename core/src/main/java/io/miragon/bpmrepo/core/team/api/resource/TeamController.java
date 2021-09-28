package io.miragon.bpmrepo.core.team.api.resource;


import io.miragon.bpmrepo.core.team.api.mapper.TeamApiMapper;
import io.miragon.bpmrepo.core.team.api.transport.NewTeamTO;
import io.miragon.bpmrepo.core.team.api.transport.TeamTO;
import io.miragon.bpmrepo.core.team.api.transport.TeamUpdateTO;
import io.miragon.bpmrepo.core.team.domain.facade.TeamFacade;
import io.miragon.bpmrepo.core.team.domain.model.Team;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

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

    /**
     * Update a Team
     *
     * @param teamId       Id of the team
     * @param teamUpdateTO Update that should be saved
     * @return updated Team
     */
    @Operation(summary = "Update a Team")
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamTO> updateTeam(@PathVariable @NotBlank final String teamId,
                                             @RequestBody @Valid final TeamUpdateTO teamUpdateTO) {
        log.debug("Updating Team");
        final Team team = this.teamFacade.updateTeam(teamId, this.apiMapper.mapToModel(teamUpdateTO));
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(team));
    }

    /**
     * Get all assigned Teams
     *
     * @return list of Teams the user is assigned to
     */
    @Operation(summary = "Get all assigned Teams")
    @GetMapping()
    public ResponseEntity<List<TeamTO>> getAllTeams() {
        log.debug("Returning all assigned Teams");
        final List<Team> teams = this.teamFacade.getAllTeams(this.userService.getUserIdOfCurrentUser());
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(teams));
    }

    /**
     * Get one Team
     *
     * @param teamId Id of the requested Team
     * @return team-TO
     */
    @Operation(summary = "Get a single Team by providing its ID")
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamTO> getTeam(@PathVariable @NotBlank final String teamId) {
        log.debug("Returning single Team with Id {}", teamId);
        final Team team = this.teamFacade.getTeam(teamId);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(team));
    }

    /**
     * Search for Teams by name
     *
     * @param typedName the provided string that should be matched
     * @return lise of matching teams
     */
    @Operation(summary = "Search for Teams by name")
    @GetMapping("/search/{typedName}")
    public ResponseEntity<List<TeamTO>> searchTeams(@PathVariable final String typedName) {
        log.debug("Search for teams \"{}\"", typedName);
        final List<Team> teams = this.teamFacade.searchTeams(typedName);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(teams));
    }

    /**
     * Delete a Team (only callable by team owner)
     *
     * @param teamId the Id of the team
     */
    @Operation(summary = "Delete a Team")
    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable @NotBlank final String teamId) {
        log.debug("Deleting Team with ID {}", teamId);
        this.teamFacade.deleteTeam(teamId);
        return ResponseEntity.ok().build();
    }

}











