package io.miragon.bpmrepo.core.team.api.resource;


import io.miragon.bpmrepo.core.team.api.mapper.RepoTeamAssignmentApiMapper;
import io.miragon.bpmrepo.core.team.api.transport.RepoTeamAssignmentTO;
import io.miragon.bpmrepo.core.team.domain.facade.RepoTeamAssignmentFacade;
import io.miragon.bpmrepo.core.team.domain.model.RepoTeamAssignment;
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

@Slf4j
@Validated
@Transactional
@RestController
@RequiredArgsConstructor
@Tag(name = "RepoTeamAssignment")
@RequestMapping("/api/team/repoTeamAssignemnt")
public class RepoTeamAssignmentController {
    private final RepoTeamAssignmentFacade assignmentFacade;
    private final RepoTeamAssignmentApiMapper assignmentApiMapper;

    /**
     * Create an assignment between team and repository
     *
     * @param repoTeamAssignmentTO containing repoId, teamId and role
     * @return the created assignment
     */
    @Operation(summary = "Create an assignment between team and repository")
    @PostMapping()
    public ResponseEntity<RepoTeamAssignmentTO> createRepoTeamAssignment(@RequestBody @Valid final RepoTeamAssignmentTO repoTeamAssignmentTO) {
        log.debug("Creating new Assignment for Team {} to Repository", repoTeamAssignmentTO.getTeamId(), repoTeamAssignmentTO.getTeamId());
        final RepoTeamAssignment repoTeamAssignment = this.assignmentFacade.createAssignment(this.assignmentApiMapper.mapToModel(repoTeamAssignmentTO));
        return ResponseEntity.ok().body(this.assignmentApiMapper.mapToTO(repoTeamAssignment));
    }

    /**
     * Update an assignment between team and repository
     *
     * @param repoTeamAssignmentTO containing repoId, teamId and role
     * @return the updated Assignment
     */
    @Operation(summary = "Update an assignment between team and repository")
    @PutMapping()
    public ResponseEntity<RepoTeamAssignmentTO> updateRepoTeamAssignment(@RequestBody @Valid final RepoTeamAssignmentTO repoTeamAssignmentTO) {
        log.debug("Updating the assignment for Team {} to repository {}", repoTeamAssignmentTO.getTeamId(), repoTeamAssignmentTO.getRepositoryId());
        final RepoTeamAssignment repoTeamAssignment = this.assignmentFacade.updateAssignment(this.assignmentApiMapper.mapToModel(repoTeamAssignmentTO));
        return ResponseEntity.ok().body(this.assignmentApiMapper.mapToTO(repoTeamAssignment));
    }

    /**
     * Delete an assignment between team and repository
     *
     * @param teamId id of the team
     * @param repoId id of the repository
     */
    @Operation(summary = "Delete an assignment between team and repository")
    @DeleteMapping("/delete/{teamId}/{repoId}")
    public ResponseEntity<Void> deleteRepoTeamAssignment(@PathVariable @NotBlank final String teamId,
                                                         @PathVariable @NotBlank final String repoId) {
        log.debug("Deleting the assignment from team {} to repository {}", teamId, repoId);
        this.assignmentFacade.deleteAssignment(teamId, repoId);
        return ResponseEntity.ok().build();
    }


}
