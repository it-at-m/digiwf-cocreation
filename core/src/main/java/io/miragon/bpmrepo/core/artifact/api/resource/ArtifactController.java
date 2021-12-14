package io.miragon.bpmrepo.core.artifact.api.resource;

import io.miragon.bpmrepo.core.artifact.api.mapper.ArtifactApiMapper;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTypeTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactUpdateTO;
import io.miragon.bpmrepo.core.artifact.api.transport.NewArtifactTO;
import io.miragon.bpmrepo.core.artifact.domain.facade.ArtifactFacade;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.plugin.ArtifactTypesPlugin;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
@Tag(name = "Artifact")
@RequestMapping("/api/artifact")
public class ArtifactController {

    private final ArtifactFacade artifactFacade;
    private final UserService userService;

    private final ArtifactApiMapper apiMapper;
    private final ArtifactTypesPlugin artifactTypesPlugin;

    /**
     * Create an artifact
     *
     * @param repositoryId  Id of the repository
     * @param newArtifactTO artifact that should be created or updated
     * @return created artifact
     */
    @Operation(summary = "Create an artifact")
    @PostMapping("/{repositoryId}")
    public ResponseEntity<ArtifactTO> createArtifact(
            @PathVariable @NotBlank final String repositoryId,
            @RequestBody @Valid final NewArtifactTO newArtifactTO) {
        log.debug("Creating Artifact in Repository {}", repositoryId);
        final Artifact artifact = this.artifactFacade.createArtifact(repositoryId, this.apiMapper.mapToModel(newArtifactTO));
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(artifact));
    }

    /**
     * Update an artifact
     *
     * @param artifactId       Id of the artifact
     * @param artifactUpdateTO artifact that should be created or updated
     * @return updated artifact
     */
    @Operation(summary = "Update an artifact")
    @PutMapping("/{artifactId}")
    public ResponseEntity<ArtifactTO> updateArtifact(
            @PathVariable @NotBlank final String artifactId,
            @RequestBody @Valid final ArtifactUpdateTO artifactUpdateTO) {
        log.debug("Updating Artifact with ID {}", artifactId);
        val artifact = this.artifactFacade.updateArtifact(artifactId, this.apiMapper.mapUpdateToModel(artifactUpdateTO));
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifact));
    }

    /**
     * Delete an artifact
     *
     * @param artifactId Id of the artifact
     */
    @Operation(summary = "Delete one Artifact and all of its versions")
    @DeleteMapping("/{artifactId}")
    public ResponseEntity<Void> deleteArtifact(@PathVariable @NotBlank final String artifactId) {
        log.debug("Deleting Artifact with ID " + artifactId);
        this.artifactFacade.deleteArtifact(artifactId);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all artifacts of the given repository
     *
     * @param repositoryId Id of the repository
     * @return List of artifact
     */
    @Operation(summary = "Get all artifacts of the given repository")
    @GetMapping("/repository/{repositoryId}")
    public ResponseEntity<List<ArtifactTO>> getArtifactsFromRepo(@PathVariable @NotBlank final String repositoryId) {
        log.debug("Returning all Artifacts from Repository with ID {}", repositoryId);
        val artifacts = this.artifactFacade.getArtifactsFromRepo(repositoryId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts));
    }

    /**
     * Get single artifact
     *
     * @param artifactId Id of the artifact
     * @return artifact
     */
    @Operation(summary = "Get single artifact")
    @GetMapping("/{artifactId}")
    public ResponseEntity<ArtifactTO> getArtifact(@PathVariable @NotBlank final String artifactId) {
        log.debug("Returning artifact with ID {}", artifactId);
        val artifact = this.artifactFacade.getArtifact(artifactId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifact));
    }


    /**
     * Inverts the star-status (favorite-status) of an artifact
     *
     * @param artifactId Id of the artifact
     */
    @Operation(summary = "Inverts the star-status (favorite-status) of an artifact")
    @PostMapping("/starred/{artifactId}")
    public ResponseEntity<Void> setStarred(@PathVariable @NotBlank final String artifactId) {
        log.debug("Inversing starred-status of artifact {}", artifactId);
        this.artifactFacade.setStarred(artifactId, this.userService.getCurrentUser().getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Returns all starred artifacts.
     *
     * @return artifacts
     */
    @Operation(summary = "Returns all starred artifacts.")
    @GetMapping("/starred")
    public ResponseEntity<List<ArtifactTO>> getStarred() {
        log.debug("Returning starred artifacts");
        val artifacts = this.artifactFacade.getStarred(this.userService.getUserIdOfCurrentUser());
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts));
    }

    /**
     * Get recent artifacts
     *
     * @return artifacts
     */
    @Operation(summary = "Get recent artifacts")
    @GetMapping("/recent")
    public ResponseEntity<List<ArtifactTO>> getRecent() {
        log.debug("Returning 10 most recent artifacts from all repos");
        val artifacts = this.artifactFacade.getRecent(this.userService.getUserIdOfCurrentUser());
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts));
    }

    /**
     * Search artifacts by title.
     *
     * @param typedTitle Title to search for
     * @return List of artifacts
     */
    @Operation(summary = "Search artifacts by title.")
    @GetMapping("/search/{typedTitle}")
    public ResponseEntity<List<ArtifactTO>> searchArtifacts(@PathVariable final String typedTitle) {
        log.debug("Searching for Artifacts \"{}\"", typedTitle);
        val artifacts = this.artifactFacade.searchArtifacts(typedTitle, this.userService.getUserIdOfCurrentUser());
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts));
    }

    /**
     * Lock a Artifact for editing. After calling, the artifact is locked for 10 minutes for the active user. Call the endpoint again, to reset the 10-minutes timer.
     * Has to be called before "getSingleVersion" and "createOrUpdateVersion"
     *
     * @param artifactId Id of the artifact
     * @return Locked artifact
     */
    @Operation(summary = "Lock a Artifact for editing. After calling, the artifact is locked for 10 minutes for the active user. Call the endpoint again, to reset the 10-minutes timer. Has to be called before \"getSingleVersion\" and \"createOrUpdateVersion\"")
    @PostMapping("/{artifactId}/lock")
    public ResponseEntity<ArtifactTO> lockArtifact(@PathVariable @NotBlank final String artifactId) {
        log.debug("Locking Artifact {}", artifactId);
        val artifact = this.artifactFacade.lockArtifact(artifactId, this.userService.getCurrentUser().getUsername());
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(artifact));
    }

    /**
     * Unlock a artifact after editing is finished
     *
     * @param artifactId Id of the artifact
     * @return Unlocked artifact
     */
    @Operation(summary = "Unlock a artifact after editing is finished")
    @PostMapping("/{artifactId}/unlock")
    public ResponseEntity<ArtifactTO> unlockArtifact(@PathVariable @NotBlank final String artifactId) {
        log.debug("Unlocking Artifact {}", artifactId);
        val artifact = this.artifactFacade.unlockArtifact(artifactId);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(artifact));
    }

    /**
     * Get all available file types
     *
     * @return List of file types
     */
    @Operation(summary = "Get all available file types")
    @GetMapping()
    public ResponseEntity<List<ArtifactTypeTO>> getAllFileTypes() {
        log.debug("Returning File Types");
        val fileTypes = this.artifactTypesPlugin.getArtifactTypes();
        return ResponseEntity.ok(fileTypes);
    }

    /**
     * Copy file to other repository
     *
     * @param repositoryId     Id of the target repository
     * @param artifactId       Id of the artifact
     * @param artifactUpdateTO object containing name and description of the new artifact
     * @return copied artifact in the provided repository
     */
    @Operation(summary = "Copy file to other repository")
    @PostMapping("/copy/{repositoryId}/{artifactId}")
    public ResponseEntity<ArtifactTO> copyToRepository(
            @PathVariable @NotBlank final String repositoryId,
            @PathVariable @NotBlank final String artifactId,
            @RequestBody @Valid final ArtifactUpdateTO artifactUpdateTO) {
        log.debug("Copying artifact to repository {}", repositoryId);
        val artifact = this.artifactFacade.copyToRepository(repositoryId, artifactId, artifactUpdateTO.getName(), artifactUpdateTO.getDescription());
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(artifact));
    }

    /**
     * Get all artifacts of a specific type from a repository
     *
     * @param repositoryId Id of the repository
     * @param type         artifact type
     * @return List of artifacts
     */
    @Operation(summary = "Get all artifacts of a specific type from a repository")
    @GetMapping("{repositoryId}/{type}")
    public ResponseEntity<List<ArtifactTO>> getByRepoIdAndType(
            @PathVariable @NotBlank final String repositoryId,
            @PathVariable @NotBlank final String type) {
        log.debug("Returning Artifacts of type {} from Repository {}", type, repositoryId);
        final List<Artifact> artifacts = this.artifactFacade.getByRepoIdAndType(repositoryId, type);
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts));
    }


}
