package io.miragon.bpmnrepo.core.diagram.api.resource;


import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionTO;
import io.miragon.bpmnrepo.core.diagram.domain.facade.BpmnDiagramVersionFacade;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionUploadTO;
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
@Transactional
@Validated
@RequiredArgsConstructor
@RequestMapping("api/version")
public class BpmnDiagramVersionController {

    private final BpmnDiagramVersionFacade bpmnDiagramVersionFacade;

    /** Version erstellen (bzw updaten, indem die VersionId im Body mitgegeben wird). Nach diesem Call "updatePreviewSVG" in DiagramController aufrufen!
     *
     * @param bpmnRepositoryId
     * @param bpmnDiagramId
     * @param bpmnDiagramVersionUploadTO
     * @return
     */
    //returns the id of the version that has just been saved
    @PostMapping("/{bpmnRepositoryId}/{bpmnDiagramId}")
    public ResponseEntity<Void> createOrUpdateVersion(@PathVariable @NotBlank String bpmnRepositoryId,
                                              @PathVariable @NotBlank String bpmnDiagramId,
                                              @RequestBody @Valid BpmnDiagramVersionUploadTO bpmnDiagramVersionUploadTO){
        String bpmnDiagramVersionId = bpmnDiagramVersionFacade.createOrUpdateVersion(bpmnRepositoryId, bpmnDiagramId, bpmnDiagramVersionUploadTO);
        log.debug(String.format("Current versionId: %s", bpmnDiagramVersionId));
        return ResponseEntity.ok().build();
    }


    /** aktuellste Version abfragen
     *
     * @param bpmnRepositoryId
     * @param bpmnDiagramId
     * @return
     */
    //get the latest version of a diagram
    @GetMapping("/{bpmnRepositoryId}/{bpmnDiagramId}/latest")
    @Operation(summary = "Return the latest version of the requested diagram")
    public ResponseEntity<BpmnDiagramVersionTO> getLatestVersion(@PathVariable @NotBlank final String bpmnRepositoryId,
                                                                 @PathVariable @NotBlank final String bpmnDiagramId){
        log.debug("Returning latest version");
        return ResponseEntity.ok().body(this.bpmnDiagramVersionFacade.getLatestVersion(bpmnRepositoryId, bpmnDiagramId));
    }


    /** Liste aller Versionen eines Diagramms abfragen
     *
     * @param bpmnRepositoryId
     * @param bpmnDiagramId
     * @return
     */
    //get all versions by providing the corresponding parent diagram id
    @GetMapping("/{bpmnRepositoryId}/{bpmnDiagramId}/all")
    public ResponseEntity<List<BpmnDiagramVersionTO>> getAllVersions(@PathVariable @NotBlank final String bpmnRepositoryId,
                                                                     @PathVariable @NotBlank final String bpmnDiagramId){
        log.debug("Returning all Versions");
        return ResponseEntity.ok().body(this.bpmnDiagramVersionFacade.getAllVersions(bpmnRepositoryId, bpmnDiagramId));
    }

    /** Einzelne Version abfragen
     *
     * @param bpmnRepositoryId
     * @param bpmnDiagramId
     * @param bpmnDiagramVersionId
     * @return
     */
    @GetMapping("/{bpmnRepositoryId}/{bpmnDiagramId}/{bpmnDiagramVersionId}")
    public ResponseEntity<BpmnDiagramVersionTO> getSingleVersion(@PathVariable @NotBlank final String bpmnRepositoryId,
                                                                 @PathVariable @NotBlank final String bpmnDiagramId,
                                                                 @PathVariable @NotBlank final String bpmnDiagramVersionId){
        log.debug("Returning single Version");
        return ResponseEntity.ok().body(this.bpmnDiagramVersionFacade.getSingleVersion(bpmnRepositoryId, bpmnDiagramId, bpmnDiagramVersionId));
    }
}
