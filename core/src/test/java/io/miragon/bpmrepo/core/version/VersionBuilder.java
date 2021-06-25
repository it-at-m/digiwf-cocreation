package io.miragon.bpmrepo.core.version;

import io.miragon.bpmrepo.core.diagram.api.transport.DiagramVersionTO;
import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramVersion;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramVersionUpload;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.DiagramVersionEntity;

public class VersionBuilder {

    public static DiagramVersion buildVersion(final String versionId, final String diagramId, final String repoId, final String comment,
            final Integer release, final Integer milestone,
            final String file, final SaveTypeEnum saveTypeEnum) {
        return DiagramVersion.builder()
                .id(versionId)
                .diagramId(diagramId)
                .repositoryId(repoId)
                .comment(comment)
                .release(release)
                .milestone(milestone)
                .xml(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static DiagramVersionTO buildVersionTO(final String versionId, final String diagramId, final String repoId, final String comment,
            final Integer release, final Integer milestone,
            final String file, final SaveTypeEnum saveTypeEnum) {
        return DiagramVersionTO.builder()
                .id(versionId)
                .diagramId(diagramId)
                .repositoryId(repoId)
                .comment(comment)
                .release(release)
                .milestone(milestone)
                .xml(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static DiagramVersionEntity buildVersionEntity(
            final String versionId, final String diagramId, final String repoId, final String comment, final Integer release,
            final Integer milestone, final String file, final SaveTypeEnum saveTypeEnum) {
        return DiagramVersionEntity.builder()
                .id(versionId)
                .diagramId(diagramId)
                .repositoryId(repoId)
                .comment(comment)
                .release(release)
                .milestone(milestone)
                .xml(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static DiagramVersionUpload buildVersionUpload(final String comment, final String fileString, final SaveTypeEnum saveTypeEnum) {
        return DiagramVersionUpload.builder()
                .versionComment(comment)
                .xml(fileString)
                .saveType(saveTypeEnum)
                .build();
    }

}
