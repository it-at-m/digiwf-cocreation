package io.miragon.bpmnrepo.core.diagram.api.transport;

import com.sun.istack.Nullable;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BpmnDiagramVersionTO {

    @Nullable
    private String bpmnDiagramVersionId;

    @Nullable
    private String bpmnDiagramVersionComment;

    @Nullable
    private Integer bpmnDiagramVersionRelease;

    @Nullable
    private Integer bpmnDiagramVersionMilestone;

    @NotEmpty
    private String bpmnAsXML;

    @NotEmpty
    private SaveTypeEnum saveType;

    @NotEmpty
    private LocalDateTime updatedDate;

    @NotEmpty
    private String bpmnDiagramId;

    @NotEmpty
    private String bpmnRepositoryId;


    public BpmnDiagramVersionTO(String bpmnRepositoryId, String bpmnDiagramId, BpmnDiagramVersionUploadTO bpmnDiagramVersionUploadTO){
        this.bpmnRepositoryId = bpmnRepositoryId;
        this.bpmnDiagramId = bpmnDiagramId;
        this.bpmnDiagramVersionComment = bpmnDiagramVersionUploadTO.getBpmnDiagramVersionComment();
        this.bpmnAsXML = bpmnDiagramVersionUploadTO.getBpmnAsXML();
        this.saveType = bpmnDiagramVersionUploadTO.getSaveType();
        this.updatedDate = LocalDateTime.now();
    }
}
