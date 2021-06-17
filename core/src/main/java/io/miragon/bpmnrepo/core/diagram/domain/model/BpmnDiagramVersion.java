package io.miragon.bpmnrepo.core.diagram.domain.model;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionTO;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BpmnDiagramVersion {
    private String bpmnDiagramVersionId;
    private String bpmnDiagramVersionComment;
    private String bpmnDiagramId;
    private String bpmnRepositoryId;
    private Integer bpmnDiagramVersionRelease;
    private Integer bpmnDiagramVersionMilestone;
    private String bpmnDiagramVersionFile;
    private SaveTypeEnum saveType;
    private LocalDateTime updatedDate;

    public BpmnDiagramVersion(final BpmnDiagramVersionTO bpmnDiagramVersionTO) {
        this.bpmnDiagramVersionId = bpmnDiagramVersionTO.getBpmnDiagramVersionId();
        this.bpmnDiagramVersionComment = bpmnDiagramVersionTO.getBpmnDiagramVersionComment();
        this.bpmnDiagramVersionRelease = generateReleaseNumber(bpmnDiagramVersionTO);
        this.bpmnDiagramVersionMilestone = generateMilestoneNumber(bpmnDiagramVersionTO);
        this.bpmnDiagramVersionFile = bpmnDiagramVersionTO.getBpmnAsXML();
        this.bpmnDiagramId = bpmnDiagramVersionTO.getBpmnDiagramId();
        this.bpmnRepositoryId = bpmnDiagramVersionTO.getBpmnRepositoryId();
        this.saveType = bpmnDiagramVersionTO.getSaveType();
        this.updatedDate = bpmnDiagramVersionTO.getUpdatedDate();
    }


    public void updateVersion(final BpmnDiagramVersionTO bpmnDiagramVersionTO){
        if (bpmnDiagramVersionTO.getBpmnDiagramVersionComment() == null || bpmnDiagramVersionTO.getBpmnDiagramVersionComment().isEmpty()) {
            this.setBpmnDiagramVersionComment(this.getBpmnDiagramVersionComment());
        } else {
            this.setBpmnDiagramVersionComment(bpmnDiagramVersionTO.getBpmnDiagramVersionComment());
        }
        this.setBpmnDiagramVersionRelease(generateReleaseNumber(bpmnDiagramVersionTO));
        this.setBpmnDiagramVersionMilestone(generateMilestoneNumber(bpmnDiagramVersionTO));
        this.setBpmnDiagramVersionFile(bpmnDiagramVersionTO.getBpmnAsXML());
        this.setUpdatedDate(LocalDateTime.now());
    }


    public Integer generateReleaseNumber(BpmnDiagramVersionTO bpmnDiagramVersionTO){
        if(this.getBpmnDiagramVersionRelease() != null) {
            if (bpmnDiagramVersionTO.getSaveType().equals(SaveTypeEnum.RELEASE)) {
                return this.getBpmnDiagramVersionRelease() + 1;
            } else {
                return this.getBpmnDiagramVersionRelease();
            }
        }
        else{
            return 1;
        }
    }

    public Integer generateMilestoneNumber(BpmnDiagramVersionTO bpmnDiagramVersionTO){
            switch (bpmnDiagramVersionTO.getSaveType()){
                case AUTOSAVE:
                    return this.getBpmnDiagramVersionMilestone();
                case MILESTONE:
                    return this.getBpmnDiagramVersionMilestone() + 1;
                default:
                    return 0;
            }
    }
}
