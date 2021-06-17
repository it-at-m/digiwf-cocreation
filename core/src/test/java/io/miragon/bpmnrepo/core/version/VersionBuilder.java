package io.miragon.bpmnrepo.core.version;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagramVersion;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramVersionEntity;

public class VersionBuilder {

    public static BpmnDiagramVersion buildVersion(String versionId, String diagramId, String repoId, String comment, Integer release, Integer milestone, String file, SaveTypeEnum saveTypeEnum){
        return BpmnDiagramVersion.builder()
                .bpmnDiagramVersionId(versionId)
                .bpmnDiagramId(diagramId)
                .bpmnRepositoryId(repoId)
                .bpmnDiagramVersionComment(comment)
                .bpmnDiagramVersionRelease(release)
                .bpmnDiagramVersionMilestone(milestone)
                .bpmnDiagramVersionFile(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static BpmnDiagramVersionTO buildVersionTO(String versionId, String diagramId, String repoId, String comment, Integer release, Integer milestone, String file, SaveTypeEnum saveTypeEnum){
        return BpmnDiagramVersionTO.builder()
                .bpmnDiagramVersionId(versionId)
                .bpmnDiagramId(diagramId)
                .bpmnRepositoryId(repoId)
                .bpmnDiagramVersionComment(comment)
                .bpmnDiagramVersionRelease(release)
                .bpmnDiagramVersionMilestone(milestone)
                .bpmnAsXML(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static BpmnDiagramVersionEntity buildVersionEntity(String versionId, String diagramId, String repoId, String comment, Integer release, Integer milestone, String file, SaveTypeEnum saveTypeEnum){
        return BpmnDiagramVersionEntity.builder()
                .bpmnDiagramVersionId(versionId)
                .bpmnDiagramId(diagramId)
                .bpmnRepositoryId(repoId)
                .bpmnDiagramVersionComment(comment)
                .bpmnDiagramVersionRelease(release)
                .bpmnDiagramVersionMilestone(milestone)
                .bpmnDiagramVersionFile(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static BpmnDiagramVersionUploadTO buildVersionUploadTO(String comment, String fileString, SaveTypeEnum saveTypeEnum){
        return BpmnDiagramVersionUploadTO.builder()
                .bpmnDiagramVersionComment(comment)
                .bpmnAsXML(fileString)
                .saveType(saveTypeEnum)
                .build();
    }

}
