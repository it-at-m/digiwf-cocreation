package io.miragon.bpmnrepo.core.diagram;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagram;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;

import java.time.LocalDateTime;

public class DiagramBuilder {

    public static BpmnDiagram buildDiagram(final String diagramId, final String repoId, final String diagramName, final String diagramDesc, final LocalDateTime createdDate, final LocalDateTime updatedDate){
        return BpmnDiagram.builder()
                .bpmnDiagramId(diagramId)
                .bpmnRepositoryId(repoId)
                .bpmnDiagramName(diagramName)
                .bpmnDiagramDescription(diagramDesc)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    public static BpmnDiagramEntity buildDiagramEntity(final String diagramId, final String repoId, final String diagramName, final String diagramDesc, final LocalDateTime createdDate, final LocalDateTime updatedDate) {
        return BpmnDiagramEntity.builder()
                .bpmnDiagramId(diagramId)
                .bpmnRepositoryId(repoId)
                .bpmnDiagramName(diagramName)
                .bpmnDiagramDescription(diagramDesc)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    public static BpmnDiagramTO buildDiagramTO(final String diagramId, final String repoId, final String diagramName, final String diagramDesc){
        return BpmnDiagramTO.builder()
                .bpmnDiagramId(diagramId)
                .bpmnRepositoryId(repoId)
                .bpmnDiagramName(diagramName)
                .bpmnDiagramDescription(diagramDesc)
                .build();
    }

    public static BpmnDiagramUploadTO buildUploadTO(final String diagramId, final String diagramName, final String diagramDesc){
        return BpmnDiagramUploadTO.builder()
                .bpmnDiagramId(diagramId)
                .bpmnDiagramName(diagramName)
                .bpmnDiagramDescription(diagramDesc)
                .build();
    }

}
