package io.miragon.bpmrepo.core.repository.api.resource;

import io.miragon.bpmrepo.core.repository.api.mapper.RepositoryApiMapper;
import io.miragon.bpmrepo.core.repository.api.transport.NewRepositoryTO;
import io.miragon.bpmrepo.core.repository.api.transport.RepositoryTO;
import io.miragon.bpmrepo.core.repository.api.transport.RepositoryUpdateTO;
import io.miragon.bpmrepo.core.repository.domain.facade.RepositoryFacade;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
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

@Slf4j
@Validated
@Transactional
@RestController
@RequiredArgsConstructor
@Tag(name = "Repository")
@RequestMapping("/api/bpmnrepo")
public class BpmRepositoryController {

    private final RepositoryFacade repositoryFacade;

    private final RepositoryApiMapper apiMapper;

    /**
     * Create new repository
     *
     * @param newRepositoryTO repository that should be created
     */
    @PostMapping()
    @Operation(summary = "Create a new Repository")
    public ResponseEntity<Void> createRepository(@RequestBody @Valid final NewRepositoryTO newRepositoryTO) {
        log.debug("Creating new Repository");
        this.repositoryFacade.createRepository(this.apiMapper.mapNewRepository(newRepositoryTO));
        return ResponseEntity.ok().build();
    }

    /**
     * Update Repository
     *
     * @param repositoryId       Id of the repository
     * @param repositoryUpdateTO Update that should be applied
     * @return
     */
    @PutMapping("/{repositoryId}")
    @Operation(summary = "Update a Repository")
    public ResponseEntity<Void> updateRepository(@PathVariable @NotBlank final String repositoryId,
                                                 @RequestBody @Valid final RepositoryUpdateTO repositoryUpdateTO) {
        this.repositoryFacade.updateRepository(repositoryId, this.apiMapper.mapUpdate(repositoryUpdateTO));
        return ResponseEntity.ok().build();
    }

    /**
     * Alle dem user zogeordneten Repos abfragen
     *
     * @return
     */
    @GetMapping()
    @Operation(summary = "Get all Repositories")
    public ResponseEntity<List<RepositoryTO>> getAllRepositories() {
        final List<Repository> repositories = this.repositoryFacade.getAllRepositories();
        return ResponseEntity.ok(this.apiMapper.mapToTO(repositories));
    }

    /**
     * Einzelnes Repo abfragen
     *
     * @param repositoryId
     * @return
     */
    @GetMapping("/{repositoryId}")
    @Operation(summary = "Get a single Repository by providing its ID")
    public ResponseEntity<RepositoryTO> getSingleRepository(@PathVariable @NotBlank final String repositoryId) {
        log.debug(String.format("Returning single repository with id %s", repositoryId));
        final Repository repository = this.repositoryFacade.getRepository(repositoryId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(repository));
    }

    /**
     * Repository löschen (Kann nur von Ownern ausgeführt werden)
     *
     * @param repositoryId
     * @return
     */
    @DeleteMapping("/{repositoryId}")
    @Operation(summary = "Delete a Repository if you own it")
    public ResponseEntity<Void> deleteRepository(@PathVariable @NotBlank final String repositoryId) {
        log.debug("Deleting Repository with ID " + repositoryId);
        this.repositoryFacade.deleteRepository(repositoryId);
        return ResponseEntity.ok().build();
    }
}
