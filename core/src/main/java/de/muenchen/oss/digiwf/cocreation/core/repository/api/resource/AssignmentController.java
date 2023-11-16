package de.muenchen.oss.digiwf.cocreation.core.repository.api.resource;

import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.Assignment;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.service.AssignmentService;
import de.muenchen.oss.digiwf.cocreation.core.repository.api.mapper.AssignmentApiMapper;
import de.muenchen.oss.digiwf.cocreation.core.repository.api.transport.AssignmentTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Transactional
@Tag(name = "RepoAssignment")
@RequestMapping("/api/repoAssignment")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentApiMapper assignmentApiMapper;

    /**
     * Update user assignment to repository
     *
     * @param assignmentTO Assignment update
     * @return updated Assignment
     */
    @Operation(summary = "Update user assignment to repository")
    @PutMapping()
    public ResponseEntity<AssignmentTO> updateUserAssignment(@RequestBody @Valid final AssignmentTO assignmentTO) {
        log.debug("Updating Assignment for " + assignmentTO.getUserId());
        final Assignment assignment = this.assignmentService.updateAssignment(this.assignmentApiMapper.mapToModel(assignmentTO));
        return ResponseEntity.ok().body(this.assignmentApiMapper.mapToTO(assignment));
    }

    /**
     * Create user assignment to repository
     *
     * @param assignmentTO Assignment update
     * @return created Assignment
     */
    @Operation(summary = "Create user assignment to repository")
    @PostMapping()
    public ResponseEntity<AssignmentTO> createUserAssignment(@RequestBody @Valid final AssignmentTO assignmentTO) {
        log.debug("Creating new Assignment for {}", assignmentTO.getUserId());
        final Assignment assignment = this.assignmentService.createAssignment(this.assignmentApiMapper.mapToModel(assignmentTO));
        return ResponseEntity.ok().body(this.assignmentApiMapper.mapToTO(assignment));
    }


    /**
     * Delete user assignment to repository
     *
     * @param repositoryId Id of the repository
     * @param userId       User that should be removed
     * @return
     */
    @Operation(summary = "Delete user assignment to repository")
    @DeleteMapping("/{repositoryId}/{userId}")
    public ResponseEntity<Void> deleteUserAssignment(@PathVariable final String repositoryId, @PathVariable final String userId) {
        log.debug(String.format("Deleting assignment for user %s", userId));
        this.assignmentService.deleteAssignment(repositoryId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Return all user assignments for the given repository
     *
     * @param repositoryId Id of the repository
     * @return assignments
     */
    @Operation(summary = "Get all users assigned to a repository")
    @GetMapping("/{repositoryId}")
    public ResponseEntity<List<AssignmentTO>> getAllAssignedUsers(@PathVariable final String repositoryId) {
        log.debug(String.format("Returning all assigned Users for Repository %s", repositoryId));
        final List<Assignment> assignedUsers = this.assignmentService.getAllAssignedUsers(repositoryId);
        return ResponseEntity.ok(this.assignmentApiMapper.mapToTO(assignedUsers));
    }

}
