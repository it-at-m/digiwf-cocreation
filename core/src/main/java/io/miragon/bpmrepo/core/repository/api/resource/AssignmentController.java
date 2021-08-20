package io.miragon.bpmrepo.core.repository.api.resource;

import io.miragon.bpmrepo.core.repository.api.mapper.AssignmentApiMapper;
import io.miragon.bpmrepo.core.repository.api.transport.AssignmentTO;
import io.miragon.bpmrepo.core.repository.api.transport.AssignmentUpdateTO;
import io.miragon.bpmrepo.core.repository.domain.model.Assignment;
import io.miragon.bpmrepo.core.repository.domain.service.AssignmentService;
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
@Tag(name = "Assignment")
@RequestMapping("/api/assignment")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentApiMapper assignmentApiMapper;

    /**
     * Update user assignment
     *
     * @param assignmentUpdateTO Assignment update
     * @return updated Assignment
     */
    @PutMapping()
    @Operation(summary = "Update user assignment")
    public ResponseEntity<AssignmentTO> updateUserAssignment(@RequestBody @Valid final AssignmentUpdateTO assignmentUpdateTO) {
        log.debug("Creating new Assignment for " + assignmentUpdateTO.getUsername());
        final Assignment assignment = this.assignmentService.updateAssignment(this.assignmentApiMapper.mapUpdate(assignmentUpdateTO));
        return ResponseEntity.ok().body(this.assignmentApiMapper.mapToTO(assignment));
    }

    /**
     * Create user assignment
     *
     * @param assignmentUpdateTO Assignment update
     * @return created Assignment
     */
    @PostMapping
    @Operation(summary = "Create user assignment")
    public ResponseEntity<AssignmentTO> createUserAssignment(@RequestBody @Valid final AssignmentUpdateTO assignmentUpdateTO) {
        log.debug("Creating new Assignment for " + assignmentUpdateTO.getUsername());
        final Assignment assignment = this.assignmentService.createAssignment(this.assignmentApiMapper.mapUpdate(assignmentUpdateTO));
        return ResponseEntity.ok().body(this.assignmentApiMapper.mapToTO(assignment));
    }


    /**
     * Delete user assignment
     *
     * @param repositoryId Id of the repository
     * @param username     User that should be removed
     */
    @DeleteMapping("/{repositoryId}/{username}")
    @Operation(summary = "Delete user assignment")
    public ResponseEntity<Void> deleteUserAssignment(@PathVariable final String repositoryId, @PathVariable final String username) {
        log.debug(String.format("Deleting assignment for user %s", username));
        this.assignmentService.deleteAssignment(repositoryId, username);
        return ResponseEntity.ok().build();
    }

    /**
     * Return all user assignments for the given repository
     *
     * @param repositoryId Id of the repository
     * @return assignments
     */
    @GetMapping("/{repositoryId}")
    @Operation(summary = "Get all assigned users")
    public ResponseEntity<List<AssignmentTO>> getAllAssignedUsers(@PathVariable final String repositoryId) {
        log.debug(String.format("Returning all assigned Users for Repository %s", repositoryId));
        final List<io.miragon.bpmrepo.core.repository.domain.model.Assignment> assignedUsers = this.assignmentService.getAllAssignedUsers(repositoryId);
        return ResponseEntity.ok(this.assignmentApiMapper.mapToTO(assignedUsers));
    }

}
