package de.muenchen.oss.digiwf.cocreation.core.sharing.api.resource;

import de.muenchen.oss.digiwf.cocreation.core.artifact.api.mapper.ArtifactApiMapper;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Artifact;
import de.muenchen.oss.digiwf.cocreation.core.sharing.api.transport.ShareWithRepositoryTO;
import de.muenchen.oss.digiwf.cocreation.core.sharing.api.transport.SharedRepositoryTO;
import de.muenchen.oss.digiwf.cocreation.core.sharing.domain.facade.ShareFacade;
import de.muenchen.oss.digiwf.cocreation.core.sharing.domain.model.ShareWithRepository;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@Transactional
@RestController
@RequiredArgsConstructor
@Tag(name = "Share")
@RequestMapping("/api/share")
public class ShareController {

    private final ShareFacade shareFacade;
    private final ArtifactApiMapper artifactApiMapper;
    private final UserService userService;
    private final SharedApiMapper apiMapper;

    /**
     * Share an artifact with another repository
     *
     * @param shareWithRepositoryTO Object containing Ids of artifact and repository and corresponding Role
     * @return created share-object
     */
    @Operation(summary = "Share an artifact with all members of another repository")
    @PostMapping("/repository")
    public ResponseEntity<ShareWithRepositoryTO> shareWithRepository(@RequestBody @Valid final ShareWithRepositoryTO shareWithRepositoryTO) {
        log.debug("Sharing Artifact {} with repository {}", shareWithRepositoryTO.getArtifactId(), shareWithRepositoryTO.getRepositoryId());
        final ShareWithRepository shared = this.shareFacade.shareWithRepository(this.apiMapper.mapToShareRepoModel(shareWithRepositoryTO));
        return ResponseEntity.ok().body(this.apiMapper.mapToShareRepoTO(shared));
    }

    /**
     * Update the role of a sharing-relation with a repository
     *
     * @param shareWithRepositoryTO Object containing Ids of artifact and corresponding Role
     * @return created share-object
     */
    @Operation(summary = "Update the share-role of a relation with a repository")
    @PutMapping("/repository")
    public ResponseEntity<ShareWithRepositoryTO> updateShareWithRepository(@RequestBody @Valid final ShareWithRepositoryTO shareWithRepositoryTO) {
        log.debug("Sharing Artifact {} with repository {}", shareWithRepositoryTO.getArtifactId(), shareWithRepositoryTO.getRepositoryId());
        final ShareWithRepository shared = this.shareFacade.updateShareWithRepository(this.apiMapper.mapToShareRepoModel(shareWithRepositoryTO));
        return ResponseEntity.ok().body(this.apiMapper.mapToShareRepoTO(shared));
    }

    /**
     * Delete share-relation of an artifact and a repository
     *
     * @param artifactId   Id of the artifact
     * @param repositoryId Id of the repository
     */
    @Operation(summary = "Delete the sharing-relation to a specific repository")
    @DeleteMapping("/repository/unshare/{artifactId}/{repositoryId}")
    public ResponseEntity<Void> unshareArtifactWithRepository(@PathVariable @Valid final String artifactId,
                                                              @PathVariable @Valid final String repositoryId) {
        log.debug("Removing share-relation of artifact {} with repository {}", artifactId, repositoryId);
        this.shareFacade.unshareWithRepository(artifactId, repositoryId);
        return ResponseEntity.ok().build();
    }


    /**
     * Get all artifacts that are shared with the current user
     *
     * @return List of artifacts
     */
    @Operation(summary = "Get all shared Artifacts")
    @GetMapping("/artifacts")
    public ResponseEntity<List<ArtifactTO>> getAllSharedArtifacts() {
        log.debug("Returning all Artifacts shared with current user");
        final List<Artifact> sharedArtifacts = this.shareFacade.getAllSharedArtifacts(this.userService.getUserIdOfCurrentUser());
        return ResponseEntity.ok().body(this.artifactApiMapper.mapToTO(sharedArtifacts));
    }

    /**
     * Get artifacts that are shared with a repository
     *
     * @param repositoryId Id of the repository
     * @return List of artifacts
     */
    @Operation(summary = "Get Artifacts shared with Repository")
    @GetMapping("/repository/{repositoryId}")
    public ResponseEntity<List<ArtifactTO>> getSharedArtifacts(@PathVariable @NotBlank final String repositoryId) {
        log.debug("Returning Artifacts shared with Repository {}", repositoryId);
        final List<Artifact> sharedArtifacts = this.shareFacade.getArtifactsSharedWithRepository(repositoryId);
        return ResponseEntity.ok().body(sharedArtifacts.stream().map(this.artifactApiMapper::mapToTO).collect(Collectors.toList()));
    }

    /**
     * Get shared artifacts from repository by type
     *
     * @param repositoryId Id of the repository
     * @param type         artifact file type
     */
    @Operation(summary = "Get shared artifacts from repository by type")
    @GetMapping("/repository/{repositoryId}/type/{type}")
    public ResponseEntity<List<ArtifactTO>> getSharedArtifactsFromRepositoryByType(@PathVariable @NotBlank final String repositoryId,
                                                                                   @PathVariable @NotBlank final String type) {
        log.debug("Returning aritfacts of type {} shared with repository {}", type, repositoryId);
        final List<Artifact> sharedArtifacts = this.shareFacade.getArtifactsSharedWithRepositoryByType(repositoryId, type);
        return ResponseEntity.ok().body(this.artifactApiMapper.mapToTO(sharedArtifacts));
    }


    /**
     * Get all artifacts that are shared via diverse repositories and filter by artifactType
     *
     * @param type artifact file type
     * @return List of artifacts
     */

    @Operation(summary = "Get all artifacts that are shared via diverse repositories and filter by artifactType")
    @GetMapping("/artifacts/{type}")
    public ResponseEntity<List<ArtifactTO>> getSharedArtifactsByType(@PathVariable @NotBlank final String type) {
        log.debug("Returning shared Artifacts of type {}", type);
        final List<Artifact> sharedArtifacts = this.shareFacade.getSharedArtifactsByType(this.userService.getUserIdOfCurrentUser(), type);
        return ResponseEntity.ok().body(this.artifactApiMapper.mapToTO(sharedArtifacts));
    }


    /**
     * Returns all repositories that can access the specified artifact
     *
     * @param artifactId Id of the artifact
     * @return List of repositories
     */
    @GetMapping("/relations/repository/{artifactId}")
    @Operation(summary = "Get all repositories that can access a specific artifact (Admin permission required)")
    public ResponseEntity<List<SharedRepositoryTO>> getSharedRepositories(@PathVariable @NotBlank final String artifactId) {
        log.debug("Returning all repositories that can access artifact {}", artifactId);
        return ResponseEntity.ok().body(this.shareFacade.getSharedRepositories(artifactId));
    }

}
