package io.miragon.bpmrepo.core.artifact.api.resource;

import io.miragon.bpmrepo.core.artifact.api.mapper.ArtifactApiMapper;
import io.miragon.bpmrepo.core.artifact.api.mapper.SharedApiMapper;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ShareWithRepositoryTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ShareWithTeamTO;
import io.miragon.bpmrepo.core.artifact.domain.facade.ShareFacade;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.Shared;
import io.miragon.bpmrepo.core.repository.api.mapper.RepositoryApiMapper;
import io.miragon.bpmrepo.core.repository.api.transport.RepositoryTO;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
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
    private final SharedApiMapper apiMapper;
    private final RepositoryApiMapper repositoryApiMapper;
    private final ArtifactApiMapper artifactApiMapper;

    private final UserService userService;


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
        final Shared shared = this.shareFacade.shareWithRepository(shareWithRepositoryTO);
        return ResponseEntity.ok().body(this.apiMapper.mapToShareRepoTO(shared));
    }

    /**
     * Update the role of a sharing-relation with a repository
     *
     * @param shareWithRepositoryTO Object containing Ids of artifact and team and corresponding Role
     * @return created share-object
     */
    @Operation(summary = "Update the share-role of a relation with a repository")
    @PutMapping("/repository")
    public ResponseEntity<ShareWithRepositoryTO> UpdateShareWithRepository(@RequestBody @Valid final ShareWithRepositoryTO shareWithRepositoryTO) {
        log.debug("Sharing Artifact {} with repository {}", shareWithRepositoryTO.getArtifactId(), shareWithRepositoryTO.getRepositoryId());
        final Shared shared = this.shareFacade.updateShareWithRepository(shareWithRepositoryTO);
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
     * Share an artifact with a team
     *
     * @param shareWithTeamTO Object containing Ids of artifact and team and corresponding Role
     * @return created share-object
     */
    @Operation(summary = "Share an artifact with all members of another team")
    @PostMapping("/team")
    public ResponseEntity<ShareWithTeamTO> shareWithTeam(@RequestBody @Valid final ShareWithTeamTO shareWithTeamTO) {
        log.debug("Sharing Artifact {} with repository {}", shareWithTeamTO.getArtifactId(), shareWithTeamTO.getTeamId());
        final Shared shared = this.shareFacade.shareWithTeam(shareWithTeamTO);
        return ResponseEntity.ok().body(this.apiMapper.mapToShareTeamTO(shared));
    }


    /**
     * Update the role of a sharing-relation with a repository
     *
     * @param shareWithTeamTO Object containing Ids of artifact and team and corresponding Role
     * @return created share-object
     */
    @Operation(summary = "Update the share-role of a relation with a team")
    @PutMapping("/team")
    public ResponseEntity<ShareWithTeamTO> UpdateShareWithTeam(@RequestBody @Valid final ShareWithTeamTO shareWithTeamTO) {
        log.debug("Sharing Artifact {} with repository {}", shareWithTeamTO.getArtifactId(), shareWithTeamTO.getTeamId());
        final Shared shared = this.shareFacade.updateShareWithTeam(shareWithTeamTO);
        return ResponseEntity.ok().body(this.apiMapper.mapToShareTeamTO(shared));
    }

    /**
     * Delete share-relation of an artifact and a team
     *
     * @param artifactId Id of the artifact
     * @param teamId     Id of the team
     */
    @Operation(summary = "Delete the sharing-relation to a specific repository")
    @DeleteMapping("/team/unshare/{artifactId}/{teamId}")
    public ResponseEntity<Void> unshareArtifactWithTeam(@PathVariable @Valid final String artifactId,
                                                        @PathVariable @Valid final String teamId) {
        log.debug("Removing share-relation of artifact {} with team {}", artifactId, teamId);
        this.shareFacade.unshareWithTeam(artifactId, teamId);
        return ResponseEntity.ok().build();
    }


    /**
     * Get all artifacts that are shared with the current user
     *
     * @return List of artifacts
     */
    @Operation(summary = "Get all shared Artifacts")
    @GetMapping("/")
    public ResponseEntity<List<ArtifactTO>> getAllSharedArtifacts() {
        log.debug("Returning all Artifacts shared with current user");
        //TODO: Throw custom Error if no shared Artifacts could be found
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
    @GetMapping("/{repositoryId}")
    public ResponseEntity<List<ArtifactTO>> getSharedArtifacts(@PathVariable @NotBlank final String repositoryId) {
        log.debug("Returning Artifacts shared with Repository {}", repositoryId);
        final List<Artifact> sharedArtifacts = this.shareFacade.getSharedArtifacts(repositoryId);
        return ResponseEntity.ok().body(sharedArtifacts.stream().map(this.artifactApiMapper::mapToTO).collect(Collectors.toList()));
    }

    /**
     * Returns all repositories that can access the specified artifact
     *
     * @param artifactId Id of the artifact
     * @return List of repositories
     */
    @GetMapping("/repository/{artifactId}")
    @Operation(summary = "Get all repositories that can access a specific artifact")
    public ResponseEntity<List<RepositoryTO>> getSharedRepositories(@PathVariable @NotBlank final String artifactId) {
        log.debug("Returning all repositories that can access artifact {}", artifactId);
        final List<Repository> repositories = this.shareFacade.getSharedRepositories(artifactId);
        return ResponseEntity.ok().body(this.repositoryApiMapper.mapToTO(repositories));
    }

}
