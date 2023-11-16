package de.muenchen.oss.digiwf.cocreation.core.repository.api.resource;

import de.muenchen.oss.digiwf.cocreation.core.repository.api.transport.NewRepositoryTO;
import de.muenchen.oss.digiwf.cocreation.core.repository.api.transport.RepositoryTO;
import de.muenchen.oss.digiwf.cocreation.core.repository.api.transport.RepositoryUpdateTO;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.facade.RepositoryFacade;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.Repository;
import de.muenchen.oss.digiwf.cocreation.core.repository.api.mapper.RepositoryApiMapper;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.ObjectNotFoundException;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Validated
@Transactional
@RestController
@RequiredArgsConstructor
@Tag(name = "Repository")
@RequestMapping("/api/repo")
public class RepositoryController {

    private final RepositoryFacade repositoryFacade;
    private final RepositoryApiMapper apiMapper;
    private final UserService userService;

    /**
     * Create new repository
     *
     * @param newRepositoryTO repository that should be created
     * @return the created repository
     */
    @Operation(summary = "Create a new Repository")
    @PostMapping()
    public ResponseEntity<RepositoryTO> createRepository(@RequestBody @Valid final NewRepositoryTO newRepositoryTO) {
        log.debug("Creating new Repository");
        final Repository repository = this.repositoryFacade
                .createRepository(this.apiMapper.mapToModel(newRepositoryTO), this.userService.getUserIdOfCurrentUser());
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(repository));
    }

    /**
     * Update Repository
     *
     * @param repositoryId       Id of the repository
     * @param repositoryUpdateTO Update that should saved
     * @return updated Repository
     */
    @Operation(summary = "Update a Repository")
    @PutMapping("/{repositoryId}")
    public ResponseEntity<RepositoryTO> updateRepository(@PathVariable @NotBlank final String repositoryId,
                                                         @RequestBody @Valid final RepositoryUpdateTO repositoryUpdateTO) {
        log.debug("Updating Repository");
        final Repository repository = this.repositoryFacade.updateRepository(repositoryId, this.apiMapper.mapToModel(repositoryUpdateTO));
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(repository));
    }

    /**
     * Returns all repositories assigned to the current user
     *
     * @return List of repositories
     */
    @Operation(summary = "Get all Repositories")
    @GetMapping()
    public ResponseEntity<List<RepositoryTO>> getAllRepositories() {
        log.debug("Returning all assigned Repositories");
        final List<Repository> repositories = this.repositoryFacade.getAllRepositories(this.userService.getUserIdOfCurrentUser());
        return ResponseEntity.ok(this.apiMapper.mapToTO(repositories));
    }

    /**
     * Returns single repository
     *
     * @param repositoryId id of the repository
     * @return one repository
     */
    @Operation(summary = "Get a single Repository by providing its ID")
    @GetMapping("/{repositoryId}")
    public ResponseEntity<RepositoryTO> getSingleRepository(@PathVariable @NotBlank final String repositoryId) {
        log.debug(String.format("Returning single Repository with id %s", repositoryId));
        final Repository repository = this.repositoryFacade.getRepository(repositoryId).orElseThrow(() -> new ObjectNotFoundException("exception.repositoryNotFound"));
        return ResponseEntity.ok(this.apiMapper.mapToTO(repository));
    }

    /**
     * Returns all Repositories to which the current user is assigned as ADMIN or OWNER
     *
     * @return List of repositories
     */
    @Operation(summary = "Get all repositories that can be managed")
    @GetMapping("/administration")
    public ResponseEntity<List<RepositoryTO>> getManageableRepositories() {
        log.debug("Returning all Repositories with access right ADMIN or OWNER");
        final List<Repository> repositories = this.repositoryFacade.getManageableRepositories(this.userService.getUserIdOfCurrentUser());
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(repositories));
    }

    /**
     * Delete a repository (only callable by Repository owner)
     *
     * @param repositoryId id of the repository that should be deleted
     */
    @Operation(summary = "Delete a Repository if you own it")
    @DeleteMapping("/{repositoryId}")
    public ResponseEntity<Void> deleteRepository(@PathVariable @NotBlank final String repositoryId) {
        log.warn("Deleting Repository with ID " + repositoryId);
        this.repositoryFacade.deleteRepository(repositoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * Search for repositories by name
     *
     * @param typedName the provided string that should be matched
     * @return list of matching repositories
     */
    @Operation(summary = "Search for repositories by name")
    @GetMapping("/search/{typedName}")
    public ResponseEntity<List<RepositoryTO>> searchRepositories(@PathVariable final String typedName) {
        log.debug("Search for repositories \"{}\"", typedName);
        final List<Repository> repositories = this.repositoryFacade.searchRepositories(typedName);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(repositories));

    }

    @Operation(summary = "Download a repository based on its name")
    @GetMapping("/{repositoryId}/download")
    public ResponseEntity<Resource> zipDownloadProject(
            @PathVariable @NotBlank final String repositoryId) throws RuntimeException {
        log.debug("Downloading Project {}", repositoryId);
        final HttpHeaders headers = this.repositoryFacade.getHeaders(repositoryId);
        final ByteArrayResource resource = this.repositoryFacade.download(repositoryId);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
