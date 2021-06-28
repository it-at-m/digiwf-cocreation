package io.miragon.bpmrepo.core.diagram.api.resource;

import io.miragon.bpmrepo.core.diagram.api.mapper.DiagramApiMapper;
import io.miragon.bpmrepo.core.diagram.api.transport.DiagramSVGUploadTO;
import io.miragon.bpmrepo.core.diagram.api.transport.DiagramTO;
import io.miragon.bpmrepo.core.diagram.api.transport.DiagramUpdateTO;
import io.miragon.bpmrepo.core.diagram.api.transport.NewDiagramTO;
import io.miragon.bpmrepo.core.diagram.domain.facade.DiagramFacade;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
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
public class DiagramController {

    private final DiagramFacade diagramFacade;
    private final UserService userService;

    private final DiagramApiMapper apiMapper;

    /**
     * Create a diagram
     *
     * @param repositoryId Id of the repository
     * @param newDiagramTO diagram that should be created or updated
     * @return created diagram
     */
    @PostMapping("/{repositoryId}")
    public ResponseEntity<DiagramTO> createDiagram(@PathVariable @NotBlank final String repositoryId, @RequestBody @Valid final NewDiagramTO newDiagramTO) {
        log.debug("Creating or updating Diagram");
        val diagram = this.diagramFacade.createDiagram(repositoryId, this.apiMapper.mapToModel(newDiagramTO));
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(diagram));
    }

    /**
     * Update a diagram
     *
     * @param diagramId       Id of the diagram
     * @param diagramUpdateTO diagram that should be created or updated
     * @return updated diagram
     */
    @PutMapping("/{diagramId}")
    public ResponseEntity<DiagramTO> updateDiagram(@PathVariable @NotBlank final String diagramId, @RequestBody @Valid final DiagramUpdateTO diagramUpdateTO) {
        log.debug("Creating or updating Diagram");
        val diagram = this.diagramFacade.updateDiagram(diagramId, this.apiMapper.mapUpdateToModel(diagramUpdateTO));
        return ResponseEntity.ok(this.apiMapper.mapToTO(diagram));
    }

    /**
     * Delete diagram
     *
     * @param diagramId Id of the diagram
     */
    @DeleteMapping("/{diagramId}")
    @Operation(summary = "Delete one Diagram and all of its versions")
    public ResponseEntity<Void> deleteDiagram(@PathVariable @NotBlank final String diagramId) {
        log.debug("Deleting Diagram with ID " + diagramId);
        this.diagramFacade.deleteDiagram(diagramId);
        return ResponseEntity.ok().build();
    }

    /**
     * All diagrams of the given repository
     *
     * @param repositoryId Id of the repository
     * @return diagrams
     */
    @GetMapping("/repository/{repositoryId}")
    public ResponseEntity<List<DiagramTO>> getDiagramsFromRepo(@PathVariable @NotBlank final String repositoryId) {
        log.debug(String.format("Returning diagrams from repository %s", repositoryId));
        val diagrams = this.diagramFacade.getDiagramsFromRepo(repositoryId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(diagrams));
    }

    /**
     * Get single diagram
     *
     * @param diagramId Id of the diagram
     * @return diagram
     */
    @GetMapping("/{diagramId}")
    public ResponseEntity<DiagramTO> getDiagram(@PathVariable @NotBlank final String diagramId) {
        log.debug("Returning diagram with ID " + diagramId);
        val diagram = this.diagramFacade.getDiagram(diagramId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(diagram));
    }

    /**
     * Update the preview svg of a diagram
     *
     * @param diagramId          Id of the diagram
     * @param diagramSVGUploadTO Svg upload
     */
    @PostMapping("/{diagramId}")
    public ResponseEntity<Void> updatePreviewSVG(
            @PathVariable @NotBlank final String diagramId,
            @RequestBody @Valid final DiagramSVGUploadTO diagramSVGUploadTO) {
        log.debug("Updating SVG-preview picture");
        this.diagramFacade.updatePreviewSVG(diagramId, diagramSVGUploadTO.getSvgPreview());
        return ResponseEntity.ok().build();
    }

    /**
     * Stars or unstars the given diagram.
     *
     * @param diagramId Id of the diagram
     */
    @PostMapping("/starred/{diagramId}")
    public ResponseEntity<Void> setStarred(@PathVariable @NotBlank final String diagramId) {
        log.debug(String.format("Inversing starred-status of diagram %s", diagramId));
        this.diagramFacade.setStarred(diagramId, this.userService.getCurrentUser().getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Returns all starred diagrams.
     *
     * @return diagrams
     */
    @GetMapping("/starred")
    public ResponseEntity<List<DiagramTO>> getStarred() {
        log.debug("Returning starred diagrams");
        val diagrams = this.diagramFacade.getStarred();
        return ResponseEntity.ok(this.apiMapper.mapToTO(diagrams));
    }

    /**
     * Get recent diagrams
     *
     * @return diagrams
     */
    @GetMapping("/recent")
    public ResponseEntity<List<DiagramTO>> getRecent() {
        log.debug("Returning 10 most recent diagrams from all repos");
        val diagrams = this.diagramFacade.getRecent();
        return ResponseEntity.ok(this.apiMapper.mapToTO(diagrams));
    }

    /**
     * Search diagrams by title.
     *
     * @param typedTitle Title to search for
     * @return diagrams
     */
    @GetMapping("/search/{typedTitle}")
    public ResponseEntity<List<DiagramTO>> searchDiagrams(@PathVariable final String typedTitle) {
        log.debug(String.format("Searching for Diagrams \"%s\"", typedTitle));
        val diagrams = this.diagramFacade.searchDiagrams(typedTitle);
        return ResponseEntity.ok(this.apiMapper.mapToTO(diagrams));
    }

}
