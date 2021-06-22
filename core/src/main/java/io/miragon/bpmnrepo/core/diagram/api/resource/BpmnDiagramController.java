package io.miragon.bpmnrepo.core.diagram.api.resource;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramSVGUploadTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.facade.BpmnDiagramFacade;
import io.swagger.v3.oas.annotations.Operation;
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
@RestController
@Transactional
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/diagram")
public class BpmnDiagramController {

    private final BpmnDiagramFacade bpmnDiagramFacade;

    /**
     * Neue Repos erstellen oder Name bzw. Beschreibung eines Diagrams ändern, indem die DiagramId im Body mitgegeben wird
     *
     * @param bpmnRepositoryId
     * @param bpmnDiagramUploadTO
     * @return created diagram
     */
    @PostMapping("/{bpmnRepositoryId}")
    public ResponseEntity<BpmnDiagramTO> createOrUpdateDiagram(@PathVariable @NotBlank final String bpmnRepositoryId,
            @RequestBody @Valid final BpmnDiagramUploadTO bpmnDiagramUploadTO) {
        log.debug("Creating or updating Diagram");
        val result = this.bpmnDiagramFacade.createOrUpdateDiagram(bpmnRepositoryId, bpmnDiagramUploadTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Speichern eines SVGs, das später zur Vorschau im Menü angezeigt wird. Aufruf wird von Modeler ausgeführt, nachdem user ein Diagram speichert (den createOrUpdateVersion-Endpoint aufruft) (Conversion XML SVG als String wird in Modeler ausgeführt)
     *
     * @param bpmnRepositoryId
     * @param bpmnDiagramId
     * @param bpmnDiagramSVGUploadTO
     */
    @PostMapping("/{bpmnRepositoryId}/{bpmnDiagramId}")
    public ResponseEntity<Void> updatePreviewSVG(@PathVariable @NotBlank final String bpmnRepositoryId,
            @PathVariable @NotBlank final String bpmnDiagramId,
            @RequestBody @Valid final BpmnDiagramSVGUploadTO bpmnDiagramSVGUploadTO) {
        log.debug("Updating SVG-preview picture");
        this.bpmnDiagramFacade.updatePreviewSVG(bpmnRepositoryId, bpmnDiagramId, bpmnDiagramSVGUploadTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Erster Aufruf markiert das Diagram als Favorit, zweiter Aufruf löscht die Favoritenmarkierung und so weiter
     *
     * @param bpmnDiagramId
     * @return
     */
    @PostMapping("/starred/{bpmnDiagramId}")
    public ResponseEntity<Void> setStarred(@PathVariable @NotBlank final String bpmnDiagramId) {
        log.debug(String.format("Inversing starred-status of diagram %s", bpmnDiagramId));
        this.bpmnDiagramFacade.setStarred(bpmnDiagramId);
        return ResponseEntity.ok().build();
    }

    /**
     * Gibt alle Diagramme zurück, die als Favorit markiert wurden (starred)
     *
     * @return
     */
    @GetMapping("/starred")
    public ResponseEntity<List<BpmnDiagramTO>> getStarred() {
        log.debug("Returning starred diagrams");
        return ResponseEntity.ok().body(this.bpmnDiagramFacade.getStarred());
    }

    /**
     * Alle Diagramme innerhalb eines Repos abfragen
     *
     * @param repositoryId
     * @return
     */
    @GetMapping("/all/{repositoryId}")
    public ResponseEntity<List<BpmnDiagramTO>> getDiagramsFromRepo(@PathVariable @NotBlank final String repositoryId) {
        //Exceptionhandling for n.a. repositoryId
        log.debug(String.format("Returning diagrams from repository %s", repositoryId));
        return ResponseEntity.ok().body(this.bpmnDiagramFacade.getDiagramsFromRepo(repositoryId));

    }

    /**
     * Einzelnes Diagram abfragen
     *
     * @param bpmnRepositoryId
     * @param bpmnDiagramId
     */
    @GetMapping("/{bpmnRepositoryId}/{bpmnDiagramId}")
    public ResponseEntity<BpmnDiagramTO> getSingleDiagram(@PathVariable @NotBlank final String bpmnRepositoryId,
            @PathVariable @NotBlank final String bpmnDiagramId) {
        log.debug("Returning diagram with ID " + bpmnDiagramId);
        return ResponseEntity.ok().body(this.bpmnDiagramFacade.getSingleDiagram(bpmnRepositoryId, bpmnDiagramId));
    }

    /**
     * Letzten 10 Diagramme abfragen, sortiert nach Änderungsdatum
     */
    @GetMapping("/recent10")
    public ResponseEntity<List<BpmnDiagramTO>> getRecent() {
        log.debug("Returning 10 most recent diagrams from all repos");
        return ResponseEntity.ok().body(this.bpmnDiagramFacade.getRecent());
    }

    /**
     * Diagramme per Eingabestring suchen
     *
     * @param typedTitle
     */
    @GetMapping("/searchDiagrams/{typedTitle}")
    public ResponseEntity<List<BpmnDiagramTO>> searchDiagrams(@PathVariable final String typedTitle) {
        log.debug(String.format("Searching for Diagrams \"%s\"", typedTitle));
        return ResponseEntity.ok().body(this.bpmnDiagramFacade.searchDiagrams(typedTitle));
    }

    /**
     * Ein Diagram, inklusive aller child-versionen löschen
     *
     * @param bpmnRepositoryId
     * @param bpmnDiagramId
     */
    @DeleteMapping("{bpmnRepositoryId}/{bpmnDiagramId}")
    @Operation(summary = "Delete one Diagram and all of its versions")
    public ResponseEntity<Void> deleteDiagram(@PathVariable @NotBlank final String bpmnRepositoryId,
            @PathVariable @NotBlank final String bpmnDiagramId) {
        log.debug("Deleting Diagram with ID " + bpmnDiagramId);
        this.bpmnDiagramFacade.deleteDiagram(bpmnRepositoryId, bpmnDiagramId);
        return ResponseEntity.ok().build();
    }

}
