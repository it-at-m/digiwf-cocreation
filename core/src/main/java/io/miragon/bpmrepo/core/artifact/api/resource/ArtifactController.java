package io.miragon.bpmrepo.core.artifact.api.resource;

import io.miragon.bpmrepo.core.artifact.api.mapper.ArtifactApiMapper;
import io.miragon.bpmrepo.core.artifact.api.plugin.FileTypesPlugin;
import io.miragon.bpmrepo.core.artifact.api.transport.*;
import io.miragon.bpmrepo.core.artifact.domain.facade.ArtifactFacade;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
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

    private final FileTypesPlugin fileTypesPlugin;

    /**
     * Create a artifact
     *
     * @param repositoryId  Id of the repository
     * @param newArtifactTO artifact that should be created or updated
     * @return created artifact
     */
    @PostMapping("/{repositoryId}")
    public ResponseEntity<ArtifactTO> createArtifact(@PathVariable @NotBlank final String repositoryId, @RequestBody @Valid final NewArtifactTO newArtifactTO) {
        log.debug("Creating or updating Artifact");
        val artifact = this.artifactFacade.createArtifact(repositoryId, this.apiMapper.mapToModel(newArtifactTO));
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(artifact));
    }

    /**
     * Update a artifact
     *
     * @param artifactId       Id of the artifact
     * @param artifactUpdateTO artifact that should be created or updated
     * @return updated artifact
     */
    @PutMapping("/{artifactId}")
    public ResponseEntity<ArtifactTO> updateArtifact(@PathVariable @NotBlank final String artifactId, @RequestBody @Valid final ArtifactUpdateTO artifactUpdateTO) {
        log.debug("Creating or updating Artifact");
        val artifact = this.artifactFacade.updateArtifact(artifactId, this.apiMapper.mapUpdateToModel(artifactUpdateTO));
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifact));
    }

    /**
     * Delete artifact
     *
     * @param artifactId Id of the artifact
     */
    @DeleteMapping("/{artifactId}")
    @Operation(summary = "Delete one Artifact and all of its versions")
    public ResponseEntity<Void> deleteArtifact(@PathVariable @NotBlank final String artifactId) {
        log.debug("Deleting Artifact with ID " + artifactId);
        this.artifactFacade.deleteArtifact(artifactId);
        return ResponseEntity.ok().build();
    }

    /**
     * All artifacts of the given repository
     *
     * @param repositoryId Id of the repository
     * @return artifacts
     */
    @GetMapping("/repository/{repositoryId}")
    public ResponseEntity<List<ArtifactTO>> getArtifactsFromRepo(@PathVariable @NotBlank final String repositoryId) {
        log.debug(String.format("Returning artifacts from repository %s", repositoryId));
        val artifacts = this.artifactFacade.getArtifactsFromRepo(repositoryId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts));
    }

    /**
     * Get single artifact
     *
     * @param artifactId Id of the artifact
     * @return artifact
     */
    @GetMapping("/{artifactId}")
    public ResponseEntity<ArtifactTO> getArtifact(@PathVariable @NotBlank final String artifactId) {
        log.debug("Returning artifact with ID " + artifactId);
        val artifact = this.artifactFacade.getArtifact(artifactId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifact));
    }

    /**
     * Update the preview svg of a artifact
     *
     * @param artifactId          Id of the artifact
     * @param artifactSVGUploadTO Svg upload
     */
    @PostMapping("/previewSVG/{artifactId}")
    public ResponseEntity<Void> updatePreviewSVG(
            @PathVariable @NotBlank final String artifactId,
            @RequestBody @Valid final ArtifactSVGUploadTO artifactSVGUploadTO) {
        log.debug("Updating SVG-preview picture");
        this.artifactFacade.updatePreviewSVG(artifactId, artifactSVGUploadTO.getSvgPreview());
        return ResponseEntity.ok().build();
    }

    /**
     * Stars or unstars the given artifact.
     *
     * @param artifactId Id of the artifact
     */
    @PostMapping("/starred/{artifactId}")
    public ResponseEntity<Void> setStarred(@PathVariable @NotBlank final String artifactId) {
        log.debug(String.format("Inversing starred-status of artifact %s", artifactId));
        this.artifactFacade.setStarred(artifactId, this.userService.getCurrentUser().getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Returns all starred artifacts.
     *
     * @return artifacts
     */
    @GetMapping("/starred")
    public ResponseEntity<List<ArtifactTO>> getStarred() {
        log.debug("Returning starred artifacts");
        val artifacts = this.artifactFacade.getStarred();
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts));
    }

    /**
     * Get recent artifacts
     *
     * @return artifacts
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ArtifactTO>> getRecent() {
        log.debug("Returning 10 most recent artifacts from all repos");
        val artifacts = this.artifactFacade.getRecent();
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts));
    }

    /**
     * Search artifacts by title.
     *
     * @param typedTitle Title to search for
     * @return artifacts
     */
    @GetMapping("/search/{typedTitle}")
    public ResponseEntity<List<ArtifactTO>> searchArtifacts(@PathVariable final String typedTitle) {
        log.debug(String.format("Searching for Artifacts \"%s\"", typedTitle));
        val artifacts = this.artifactFacade.searchArtifacts(typedTitle);
        return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts));
    }

    /**
     * Lock a Artifact for editing. After calling, the artifact is locked for 10 minutes for the active user. Call the endpoint again, to reset the 10-minutes timer.
     * Has to be called before "getSingleVersion" and "createOrUpdateVersion"
     *
     * @param artifactId Id of the artifact
     * @return the userName of the user who has locked the artifact
     */
    @PostMapping("/{artifactId}/lock")
    public ResponseEntity<Void> lockArtifact(@PathVariable @NotBlank final String artifactId) {
        log.debug(String.format("Locking Artifact %s", artifactId));
        this.artifactFacade.lockArtifact(artifactId);
        return ResponseEntity.ok().build();
    }

    /**
     * Unlock a artifact after editing is finished
     *
     * @param artifactId Id of the artifact
     * @return the userName of the user who has locked the artifact
     */
    @PostMapping("/{artifactId}/unlock")
    public ResponseEntity<Void> unlockArtifact(@PathVariable @NotBlank final String artifactId) {
        log.debug(String.format("Unlocking Artifact %s", artifactId));
        this.artifactFacade.unlockArtifact(artifactId);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all available file types
     *
     * @return file types
     */
    @GetMapping
    public ResponseEntity<List<FileTypesTO>> getAllFileTypes() {
        log.warn("Fetching File Types");
        val fileTypes = this.fileTypesPlugin.getFileTypes();
        return ResponseEntity.ok(fileTypes);
    }
}
