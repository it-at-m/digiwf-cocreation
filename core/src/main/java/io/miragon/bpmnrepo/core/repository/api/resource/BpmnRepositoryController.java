package io.miragon.bpmnrepo.core.repository.api.resource;


import io.miragon.bpmnrepo.core.repository.domain.facade.BpmnRepositoryFacade;
import io.miragon.bpmnrepo.core.repository.api.transport.BpmnRepositoryRequestTO;
import io.miragon.bpmnrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.swagger.v3.oas.annotations.Operation;
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
@RestController
@RequiredArgsConstructor
@Transactional
@Validated
@RequestMapping("/api/bpmnrepo")
public class BpmnRepositoryController {

    private final BpmnRepositoryFacade bpmnRepositoryFacade;

    /** Repo erstellen
     *
     * @param newBpmnRepositoryTO
     * @return
     */
    @PostMapping()
    @Operation(summary = "Create a new Repository")
    public ResponseEntity<Void> createRepository(@RequestBody @Valid final NewBpmnRepositoryTO newBpmnRepositoryTO){
        bpmnRepositoryFacade.createRepository(newBpmnRepositoryTO);
        return ResponseEntity.ok().build();
    }

    /** Repo eigenschaften ändern (Name oder Beschreibung)
     *
     * @param bpmnRepositoryId
     * @param newBpmnRepositoryTO
     * @return
     */
    @PutMapping("/{bpmnRepositoryId}")
    @Operation(summary = "Update a Repository")
    public ResponseEntity<Void> updateRepository(@PathVariable @NotBlank final String bpmnRepositoryId,
                                                @RequestBody @Valid final NewBpmnRepositoryTO newBpmnRepositoryTO){
        bpmnRepositoryFacade.updateRepository(bpmnRepositoryId, newBpmnRepositoryTO);
        return ResponseEntity.ok().build();
    }


    /** Alle dem user zogeordneten Repos abfragen
     *
     * @return
     */
    @GetMapping()
    @Operation(summary = "Get all Repositories")
    public ResponseEntity<List<BpmnRepositoryRequestTO>> getAllRepositories(){
        log.debug("Returning all Repositories assigned to current user");
        return ResponseEntity.ok().body(this.bpmnRepositoryFacade.getAllRepositories());
    }

    /** Einzelnes Repo abfragen
     *
     * @param repositoryId
     * @return
     */
    @GetMapping("/{repositoryId}")
    @Operation(summary = "Get a single Repository by providing its ID")
    public ResponseEntity<BpmnRepositoryRequestTO> getSingleRepository(@PathVariable @NotBlank final String repositoryId){
        log.debug(String.format("Returning single repository with id %s", repositoryId));
        BpmnRepositoryRequestTO bpmnRepositoryRequestTO = this.bpmnRepositoryFacade.getSingleRepository(repositoryId);
        return ResponseEntity.ok().body(bpmnRepositoryRequestTO);
    }

    /** Repository löschen (Kann nur von Ownern ausgeführt werden)
     *
     * @param repositoryId
     * @return
     */
    @DeleteMapping("/{repositoryId}")
    @Operation(summary = "Delete a Repository if you own it")
    public ResponseEntity<Void> deleteRepository(@PathVariable @NotBlank final String repositoryId){
        log.debug("Deleting Repository with ID " + repositoryId);
        this.bpmnRepositoryFacade.deleteRepository(repositoryId);
        return ResponseEntity.ok().build();
    }
}
