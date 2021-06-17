package io.miragon.bpmnrepo.core.diagram.domain.model;


import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BpmnDiagram {

    private String bpmnDiagramId;
    private String bpmnDiagramName;
    private String bpmnDiagramDescription;
    private String bpmnRepositoryId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String svgPreview;
    private String fileType;



    public void updateDiagram(final BpmnDiagramTO bpmnDiagramTO) {
        if (bpmnDiagramTO.getBpmnDiagramName() != null && !bpmnDiagramTO.getBpmnDiagramName().isEmpty()) {
            this.setBpmnDiagramName(bpmnDiagramTO.getBpmnDiagramName());
        }
        if (bpmnDiagramTO.getBpmnDiagramDescription() != null && !bpmnDiagramTO.getBpmnDiagramDescription().isEmpty()) {
            this.setBpmnDiagramDescription(bpmnDiagramTO.getBpmnDiagramDescription());
        }
        updateDate();
    }

    public void updateDate(){
        this.updatedDate = LocalDateTime.now();
    }

}
