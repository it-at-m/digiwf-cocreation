package io.miragon.bpmrepo.core.team.api.resource;

import io.miragon.bpmrepo.core.team.api.mapper.TeamAssignmentApiMapper;
import io.miragon.bpmrepo.core.team.api.transport.TeamAssignmentTO;
import io.miragon.bpmrepo.core.team.domain.facade.TeamAssignmentFacade;
import io.miragon.bpmrepo.core.team.domain.model.TeamAssignment;
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
import java.util.List;

@Slf4j
@Validated
@Transactional
@RestController
@RequiredArgsConstructor
@Tag(name = "TeamAssignment")
@RequestMapping("/api/teamAssignment")
public class TeamAssignmentController {

    private final TeamAssignmentFacade teamAssignmentFacade;
    private final UserService userService;
    private final TeamAssignmentApiMapper apiMapper;

    /**
     * Create user assignment to team
     *
     * @param teamAssignmentTO Object containing team-Id, userId, username and role
     * @return created assignment TO
     */
    @Operation(summary = "Create user assignment to Team")
    @PostMapping()
    public ResponseEntity<TeamAssignmentTO> createTeamAssignment(@RequestBody @Valid final TeamAssignmentTO teamAssignmentTO) {
        log.debug("Creating new Assignment for {}", teamAssignmentTO.getUserId());
        final TeamAssignment teamAssignment = this.teamAssignmentFacade.createTeamAssignment(teamAssignmentTO);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(teamAssignment));
    }

    /**
     * Update team assignment to team
     *
     * @param teamAssignmentTO assignment update data, containing Ids and role
     * @return updated Assignment
     */
    @Operation(summary = "Update user assignment to Team")
    @PutMapping()
    public ResponseEntity<TeamAssignmentTO> updateTeamAssignment(@RequestBody @Valid final TeamAssignmentTO teamAssignmentTO) {
        log.debug("Updating team assignment for {}", teamAssignmentTO.getUserId());
        final TeamAssignment teamAssignment = this.teamAssignmentFacade.updateTeamAssignment(teamAssignmentTO);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(teamAssignment));
    }

    /**
     * Return all users assignments for the given team
     *
     * @param teamId Id of the team
     * @return assignments
     */
    @Operation(summary = "Get all users assigned to a team")
    @GetMapping("/{teamId}")
    public ResponseEntity<List<TeamAssignmentTO>> getAllTeamAssignedUsers(@PathVariable final String teamId) {
        log.debug("Returning all assigned users for Team {}", teamId);
        final List<TeamAssignment> assignedUsers = this.teamAssignmentFacade.getAllAssignedUsers(teamId);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(assignedUsers));
    }

    /**
     * Delete user assignment to team
     *
     * @param teamId Id of the team
     * @param userId Id of the user
     */
    @Operation(summary = "Delete user assignment to team")
    @DeleteMapping("/{teamId}/{userId}")
    public ResponseEntity<Void> deleteTeamUserAssignment(@PathVariable final String teamId, @PathVariable final String userId) {
        log.debug("Deleting assignment for user {}", userId);
        this.teamAssignmentFacade.deleteAssignment(teamId, userId);
        return ResponseEntity.ok().build();
    }

}
