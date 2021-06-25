package io.miragon.bpmrepo.core.diagram;

import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramUpdate;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.DiagramEntity;

import java.time.LocalDateTime;

public class DiagramBuilder {

    public static Diagram buildDiagram(final String diagramId, final String repoId, final String diagramName,
            final String diagramDesc,
            final LocalDateTime createdDate, final LocalDateTime updatedDate) {
        return io.miragon.bpmrepo.core.diagram.domain.model.Diagram.builder()
                .id(diagramId)
                .repositoryId(repoId)
                .name(diagramName)
                .description(diagramDesc)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    public static DiagramEntity buildDiagramEntity(final String diagramId, final String repoId, final String diagramName, final String diagramDesc,
            final LocalDateTime createdDate, final LocalDateTime updatedDate) {
        return DiagramEntity.builder()
                .id(diagramId)
                .repositoryId(repoId)
                .name(diagramName)
                .description(diagramDesc)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    public static Diagram buildDiagram(final String diagramId, final String diagramName, final String diagramDesc) {
        return Diagram.builder()
                .id(diagramId)
                .name(diagramName)
                .description(diagramDesc)
                .build();
    }

    public static DiagramUpdate buildDiagramUpdate(final String diagramName, final String diagramDesc) {
        return DiagramUpdate.builder()
                .name(diagramName)
                .description(diagramDesc)
                .build();
    }

}
