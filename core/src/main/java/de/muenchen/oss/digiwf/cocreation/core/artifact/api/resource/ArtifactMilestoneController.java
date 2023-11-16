package de.muenchen.oss.digiwf.cocreation.core.artifact.api.resource;

import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactMilestoneTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactMilestoneUpdateTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactMilestoneUploadTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.facade.ArtifactMilestoneFacade;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactMilestone;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.mapper.ArtifactMilestoneApiMapper;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.ObjectNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@Transactional
@Validated
@RequiredArgsConstructor
@Tag(name = "Milestone")
@RequestMapping("api/milestone")
public class ArtifactMilestoneController {

    private final ArtifactMilestoneFacade artifactMilestoneFacade;
    private final ArtifactMilestoneApiMapper apiMapper;

    /**
     * Get milestones by providing deploymentIds
     *
     * @return list of milestones
     */
    @Operation(summary = "Get milestones by providing deploymentIds")
    @PostMapping("/deployments")
    public ResponseEntity<List<ArtifactMilestoneTO>> getAllByDeploymentIds(@RequestBody final List<String> deploymentIds) {
        log.debug("Returning all deployed Milestones");
        final List<ArtifactMilestone> milestones = this.artifactMilestoneFacade.getAllByDeploymentIds(deploymentIds);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(milestones));
    }

    /**
     * Create a new milestone of the artifact. (The artifact has to be locked by the user to use this endpoint)
     *
     * @param artifactId                Id of the artifact
     * @param artifactMilestoneUploadTO Upload object
     * @return created milestone
     */
    @Operation(summary = "Create a new milestone of the artifact. (The artifact has to be locked by the user to use this endpoint)")
    @PostMapping("/{artifactId}")
    public ResponseEntity<ArtifactMilestoneTO> createMilestone(
            @PathVariable @NotBlank final String artifactId,
            @RequestBody @Valid final ArtifactMilestoneUploadTO artifactMilestoneUploadTO) {
        log.debug("Creating new Milestone of Artifact {}", artifactId);
        final ArtifactMilestone artifactMilestone = this.artifactMilestoneFacade.createMilestone(artifactId, this.apiMapper.mapUploadToModel(artifactMilestoneUploadTO));
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifactMilestone));
    }


    /**
     * Update milestone of the artifact. (The artifact has to be locked by the user to use this endpoint)
     *
     * @param artifactMilestoneUpdateTO Update object
     * @return updated milestone
     */
    @Operation(summary = "Update milestone of the artifact. (The artifact has to be locked by the user to use this endpoint)")
    @PutMapping("/update")
    public ResponseEntity<ArtifactMilestoneTO> updateMilestone(
            @RequestBody @Valid final ArtifactMilestoneUpdateTO artifactMilestoneUpdateTO) {
        log.debug("Updating Milestone {}", artifactMilestoneUpdateTO.getMilestoneId());
        final ArtifactMilestone artifactMilestone = this.artifactMilestoneFacade.updateMilestone(this.apiMapper.mapUpdateToModel(artifactMilestoneUpdateTO));
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifactMilestone));
    }


    /**
     * Get latest milestone
     *
     * @param artifactId Id of the artifact
     * @return latest milestone
     */
    @Operation(summary = "Return the latest milestone of the requested artifact")
    @GetMapping("/{artifactId}/milestone/latest")
    public ResponseEntity<ArtifactMilestoneTO> getLatestMilestone(@PathVariable @NotBlank final String artifactId) {
        log.debug("Returning latest milestone");
        final ArtifactMilestone latestMilestone = this.artifactMilestoneFacade.getLatestMilestone(artifactId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(latestMilestone));
    }

    /**
     * Get all milestones of the artifact
     *
     * @param artifactId Id of the artifact
     * @return all milestones
     */
    @Operation(summary = "Get all milestones of the artifact")
    @GetMapping("/{artifactId}/milestone")
    public ResponseEntity<List<ArtifactMilestoneTO>> getAllMilestones(@PathVariable @NotBlank final String artifactId) {
        log.debug("Returning all Milestones");
        val milestones = this.artifactMilestoneFacade.getAllMilestones(artifactId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(milestones));
    }

    /**
     * Get the latest milestone of the requested milestone, read-permission granted even if Artifact is locked
     *
     * @param artifactId Id of the artifact
     * @param milestone  Id of the milestone
     * @return
     */
    @Operation(summary = "Get the latest milestone of the requested milestone, read-permission granted even if Artifact is locked")
    @GetMapping("/{artifactId}/milestoneNumber/{milestone}")
    public ResponseEntity<ArtifactMilestoneTO> getByMilestoneNumber(
            @PathVariable @NotBlank final String artifactId,
            @PathVariable final Integer milestone) {
        log.debug("Returning milestone {} for artifact {}", milestone, artifactId);
        final Optional<ArtifactMilestone> artifactMilestone = this.artifactMilestoneFacade.getByMilestoneNumber(artifactId, milestone);
        if (artifactMilestone.isPresent()) {
            return ResponseEntity.ok().body(this.apiMapper.mapToTO(artifactMilestone.get()));
        } else {
            throw new ObjectNotFoundException("exception.milestoneNotFound");
        }
    }

    /**
     * Get a specific milestone, read-permission granted even if Artifact is locked
     *
     * @param artifactId  Id of the artifact
     * @param milestoneId Id of the milestone
     * @return
     */
    @Operation(summary = "Get a specific milestone, read-permission granted even if Artifact is locked")
    @GetMapping("/{artifactId}/milestone/{milestoneId}")
    public ResponseEntity<ArtifactMilestoneTO> getMilestone(
            @PathVariable @NotBlank final String artifactId,
            @PathVariable @NotBlank final String milestoneId) {
        log.debug("Returning single Milestone");
        final Optional<ArtifactMilestone> milestone = this.artifactMilestoneFacade.getMilestone(artifactId, milestoneId);
        if (milestone.isPresent()) {
            return ResponseEntity.ok().body(this.apiMapper.mapToTO(milestone.get()));
        } else {
            throw new ObjectNotFoundException("exception.milestoneNotFound");
        }
    }

    /**
     * Download a specific milestone
     *
     * @param artifactId  Id of the artifact
     * @param milestoneId Id of the milestone
     * @return
     */
    @Operation(summary = "Download a specific milestone")
    @GetMapping("/{artifactId}/{milestoneId}/download")
    public ResponseEntity<Resource> downloadMilestone(@PathVariable @NotBlank final String artifactId, @PathVariable @NotBlank final String milestoneId) {
        log.debug("Returning File of artifact-milestone {} for Download", milestoneId);
        final ByteArrayResource resource = this.artifactMilestoneFacade.downloadMilestone(artifactId, milestoneId);
        final HttpHeaders headers = this.artifactMilestoneFacade.getHeaders(artifactId);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
