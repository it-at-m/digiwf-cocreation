package io.miragon.bpmnrepo.core.diagram.domain.model;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionTO;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
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
        this.bpmnDiagramVersionRelease = this.generateReleaseNumber(bpmnDiagramVersionTO);
        this.bpmnDiagramVersionMilestone = this.generateMilestoneNumber(bpmnDiagramVersionTO);
        this.bpmnDiagramVersionFile = bpmnDiagramVersionTO.getBpmnAsXML();
        this.bpmnDiagramId = bpmnDiagramVersionTO.getBpmnDiagramId();
        this.bpmnRepositoryId = bpmnDiagramVersionTO.getBpmnRepositoryId();
        this.saveType = bpmnDiagramVersionTO.getSaveType();
        this.updatedDate = bpmnDiagramVersionTO.getUpdatedDate();
    }


    public void updateVersion(final BpmnDiagramVersionTO bpmnDiagramVersionTO) {
        if (bpmnDiagramVersionTO.getBpmnDiagramVersionComment() == null || bpmnDiagramVersionTO.getBpmnDiagramVersionComment().isEmpty()) {
            this.setBpmnDiagramVersionComment(this.getBpmnDiagramVersionComment());
        } else {
            this.setBpmnDiagramVersionComment(bpmnDiagramVersionTO.getBpmnDiagramVersionComment());
        }
        this.setBpmnDiagramVersionRelease(this.generateReleaseNumber(bpmnDiagramVersionTO));
        this.setBpmnDiagramVersionMilestone(this.generateMilestoneNumber(bpmnDiagramVersionTO));
        this.setBpmnDiagramVersionFile(bpmnDiagramVersionTO.getBpmnAsXML());
        this.setUpdatedDate(LocalDateTime.now());
    }


    public Integer generateReleaseNumber(final BpmnDiagramVersionTO bpmnDiagramVersionTO) {
        if (bpmnDiagramVersionTO.getSaveType() != null) {
            if (bpmnDiagramVersionTO.getSaveType().equals(SaveTypeEnum.RELEASE)) {
                return bpmnDiagramVersionTO.getBpmnDiagramVersionRelease() + 1;
            } else {
                return bpmnDiagramVersionTO.getBpmnDiagramVersionRelease();
            }
        } else {
            return 1;
        }
    }

    public Integer generateMilestoneNumber(final BpmnDiagramVersionTO bpmnDiagramVersionTO) {
        if (bpmnDiagramVersionTO.getSaveType().equals(SaveTypeEnum.AUTOSAVE)) {
            return bpmnDiagramVersionTO.getBpmnDiagramVersionMilestone();
        }
        if (bpmnDiagramVersionTO.getSaveType().equals(SaveTypeEnum.MILESTONE)) {
            return bpmnDiagramVersionTO.getBpmnDiagramVersionMilestone() + 1;
        } else {
            return 0;
        }
    }
}
