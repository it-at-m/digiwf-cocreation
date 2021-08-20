package io.miragon.bpmrepo.core.version;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactVersionTO;
import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersionUpdate;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersionUpload;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactVersionEntity;

public class VersionBuilder {

    public static ArtifactVersion buildVersion(final String versionId, final String artifactId, final String repoId, final String comment,
                                               final Integer milestone,
                                               final String file, final SaveTypeEnum saveTypeEnum) {
        return ArtifactVersion.builder()
                .id(versionId)
                .artifactId(artifactId)
                .repositoryId(repoId)
                .comment(comment)
                .milestone(milestone)
                .xml(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static ArtifactVersionTO buildVersionTO(final String versionId, final String artifactId, final String repoId, final String comment,
                                                   final Integer milestone,
                                                   final String file, final SaveTypeEnum saveTypeEnum) {
        return ArtifactVersionTO.builder()
                .id(versionId)
                .artifactId(artifactId)
                .repositoryId(repoId)
                .comment(comment)
                .milestone(milestone)
                .xml(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static ArtifactVersionEntity buildVersionEntity(
            final String versionId, final String artifactId, final String repoId, final String comment,
            final Integer milestone, final String file, final SaveTypeEnum saveTypeEnum) {
        return ArtifactVersionEntity.builder()
                .id(versionId)
                .artifactId(artifactId)
                .repositoryId(repoId)
                .comment(comment)
                .milestone(milestone)
                .xml(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static ArtifactVersionUpload buildVersionUpload(final String comment, final String fileString, final SaveTypeEnum saveTypeEnum) {
        return ArtifactVersionUpload.builder()
                .comment(comment)
                .xml(fileString)
                .saveType(saveTypeEnum)
                .build();
    }

    public static ArtifactVersionUpdate buildVersionUpdate(final String versionId, final String comment, final String fileString, final SaveTypeEnum saveTypeEnum) {
        return ArtifactVersionUpdate.builder()
                .versionId(versionId)
                .comment(comment)
                .xml(fileString)
                .saveType(saveTypeEnum)
                .build();
    }

}
