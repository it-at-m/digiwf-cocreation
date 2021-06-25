package io.miragon.bpmrepo.core.diagram.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Diagram {

    private String id;
    private String name;
    private String description;
    private String repositoryId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String svgPreview;
    private String fileType;

    public void updateDiagram(final DiagramUpdate diagramUpdate) {
        if (diagramUpdate.getName() != null && !diagramUpdate.getName().isEmpty()) {
            this.name = diagramUpdate.getName();
        }
        if (diagramUpdate.getDescription() != null && !diagramUpdate.getDescription().isEmpty()) {
            this.description = diagramUpdate.getDescription();
        }
        this.updateDate();
    }

    public void updateSvgPreview(final String svgPreview) {
        this.svgPreview = svgPreview;
    }

    public void updateDate() {
        this.updatedDate = LocalDateTime.now();
    }

}
