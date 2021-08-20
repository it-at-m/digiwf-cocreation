package io.miragon.bpmrepo.core.artifact.api.resource;

import io.miragon.bpmrepo.core.artifact.api.mapper.ArtifactApiMapper;
import io.miragon.bpmrepo.core.artifact.api.transport.*;
import io.miragon.bpmrepo.core.artifact.domain.facade.ArtifactFacade;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.plugin.ArtifactTypesPlugin;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
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
import java.util.Optional;

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
    @PostMapping("/{repositoryId}")
    public ResponseEntity<ArtifactTO> createArtifact(@PathVariable @NotBlank final String repositoryId, @RequestBody @Valid final NewArtifactTO newArtifactTO) {
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
    @PutMapping("/{artifactId}")
    public ResponseEntity<ArtifactTO> updateArtifact(@PathVariable @NotBlank final String artifactId,
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
    @DeleteMapping("/{artifactId}")
    @Operation(summary = "Delete one Artifact and all of its versions")
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
    @GetMapping("/repository/{repositoryId}")
    public ResponseEntity<List<ArtifactTO>> getArtifactsFromRepo(@PathVariable @NotBlank final String repositoryId) {
        log.debug(String.format("Returning all Artifacts from Repository with ID %s", repositoryId));
        val artifacts = this.artifactFacade.getArtifactsFromRepo(repositoryId);
        if (artifacts.isPresent()) {
            return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts.get()));
        } else {
            throw new ObjectNotFoundException();
        }
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
     * Update the preview svg of an artifact
     *
     * @param artifactId          Id of the artifact
     * @param artifactSVGUploadTO Svg upload
     * @return artifact
     */
    @PostMapping("/previewSVG/{artifactId}")
    public ResponseEntity<ArtifactTO> updatePreviewSVG(
            @PathVariable @NotBlank final String artifactId,
            @RequestBody @Valid final ArtifactSVGUploadTO artifactSVGUploadTO) {
        log.debug("Updating SVG-preview picture for artifact {}", artifactId);
        final Artifact artifact = this.artifactFacade.updatePreviewSVG(artifactId, artifactSVGUploadTO.getSvgPreview());
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(artifact));
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
        val artifacts = this.artifactFacade.getStarred(this.userService.getUserIdOfCurrentUser());
        if (artifacts.isPresent()) {
            return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts.get()));
        } else {
            throw new ObjectNotFoundException();
        }
    }

    /**
     * Get recent artifacts
     *
     * @return artifacts
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ArtifactTO>> getRecent() {
        log.debug("Returning 10 most recent artifacts from all repos");
        val artifacts = this.artifactFacade.getRecent(this.userService.getUserIdOfCurrentUser());
        if (artifacts.isPresent()) {
            return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts.get()));
        } else {
            throw new ObjectNotFoundException();
        }
    }

    /**
     * Search artifacts by title.
     *
     * @param typedTitle Title to search for
     * @return List of artifacts
     */
    @GetMapping("/search/{typedTitle}")
    public ResponseEntity<List<ArtifactTO>> searchArtifacts(@PathVariable final String typedTitle) {
        log.debug("Searching for Artifacts \"{}\"", typedTitle);
        val artifacts = this.artifactFacade.searchArtifacts(typedTitle, this.userService.getUserIdOfCurrentUser());
        if (artifacts.isPresent()) {
            return ResponseEntity.ok(this.apiMapper.mapToTO(artifacts.get()));
        } else {
            throw new ObjectNotFoundException();
        }
    }

    /**
     * Lock a Artifact for editing. After calling, the artifact is locked for 10 minutes for the active user. Call the endpoint again, to reset the 10-minutes timer.
     * Has to be called before "getSingleVersion" and "createOrUpdateVersion"
     *
     * @param artifactId Id of the artifact
     * @return Locked artifact
     */
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
    @GetMapping
    public ResponseEntity<List<ArtifactTypeTO>> getAllFileTypes() {
        log.debug("Returning File Types");
        val fileTypes = this.artifactTypesPlugin.getArtifactTypes();
        return ResponseEntity.ok(fileTypes);
    }

    /**
     * Copy file to other repository
     *
     * @param repositoryId Id of the target repository
     * @param artifactId   Id of the artifact
     * @return copied artifact in the provided repository
     */
    @PostMapping("/copy/{repositoryId}/{artifactId}")
    public ResponseEntity<ArtifactTO> copyToRepository(@PathVariable @NotBlank final String repositoryId, @PathVariable @NotBlank final String artifactId) {
        log.debug("Copying artifact to repository {}", repositoryId);
        val artifact = this.artifactFacade.copyToRepository(repositoryId, artifactId);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(artifact));
    }


    /**
     * Get all artifacts of a specific type from a repository
     *
     * @param repositoryId Id of the repository
     * @param type         artifact type
     * @return List of artifacts
     */
    @Operation(summary = "Get artifacts by providing repositoryId and fileType")
    @GetMapping("{repositoryId}/{type}")
    public ResponseEntity<List<ArtifactTO>> getByRepoIdAndType(@PathVariable @NotBlank final String repositoryId,
                                                               @PathVariable @NotBlank final String type) {
        log.debug("Returning Artifacts of type {} from Repository {}", type, repositoryId);
        final Optional<List<Artifact>> artifacts = this.artifactFacade.getByRepoIdAndType(repositoryId, type);
        //TODO: Throw custom Error if no file of type x is present
        return ResponseEntity.ok(artifacts.map(this.apiMapper::mapToTO).orElseThrow());
    }
}
