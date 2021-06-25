package io.miragon.bpmrepo.core.diagram.domain.model;

import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiagramVersion {
    private String id;
    private String comment;
    private String diagramId;
    private String repositoryId;
    private Integer release;
    private Integer milestone;
    private String xml;
    private SaveTypeEnum saveType;
    private LocalDateTime updatedDate;

    public void updateVersion(final DiagramVersion diagramVersion) {
        if (StringUtils.isNotBlank((diagramVersion.getComment()))) {
            this.comment = diagramVersion.getComment();
        }

        this.release = this.generateReleaseNumber(diagramVersion);
        this.milestone = this.generateMilestoneNumber(diagramVersion);
        this.xml = diagramVersion.getXml();
        this.updatedDate = LocalDateTime.now();
    }

    public void updateRelease(final Integer release) {
        this.release = release;
    }

    public void updateMilestone(final Integer milestone) {
        this.milestone = milestone;
    }

    public Integer generateReleaseNumber(final DiagramVersion diagramVersion) {
        log.warn("Generating Release Number");
        if (diagramVersion.getSaveType() != null) {
            if (diagramVersion.getSaveType().equals(SaveTypeEnum.RELEASE)) {
                if (diagramVersion.getRelease() != null) {
                    return diagramVersion.getRelease() + 1;
                } else {
                    return 1;
                }
            } else {
                if (diagramVersion.getRelease() != null) {
                    return diagramVersion.getRelease();
                } else {
                    return 1;
                }
            }
        } else {
            return 1;
        }
    }

    public Integer generateMilestoneNumber(final DiagramVersion diagramVersion) {
        if (diagramVersion.getSaveType() != null) {
            if (diagramVersion.getSaveType().equals(SaveTypeEnum.AUTOSAVE)) {
                if (diagramVersion.getMilestone() != null) {
                    return diagramVersion.getMilestone();
                } else {
                    return 0;
                }
            }
            if (diagramVersion.getSaveType().equals(SaveTypeEnum.MILESTONE)) {
                if (diagramVersion.getMilestone() != null) {
                    return diagramVersion.getMilestone() + 1;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
        return 0;
    }

}
