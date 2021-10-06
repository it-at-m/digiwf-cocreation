package io.miragon.bpmrepo.core.version;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactMilestoneTO;
import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestoneUpdate;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestoneUpload;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactMilestoneEntity;

public class VersionBuilder {

    public static ArtifactMilestone buildVersion(final String versionId, final String artifactId, final String repoId, final String comment,
                                                 final Integer milestone,
                                                 final String file, final SaveTypeEnum saveTypeEnum) {
        return ArtifactMilestone.builder()
                .id(versionId)
                .artifactId(artifactId)
                .repositoryId(repoId)
                .comment(comment)
                .milestone(milestone)
                .file(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static ArtifactMilestoneTO buildVersionTO(final String versionId, final String artifactId, final String repoId, final String comment,
                                                     final Integer milestone,
                                                     final String file, final SaveTypeEnum saveTypeEnum) {
        return ArtifactMilestoneTO.builder()
                .id(versionId)
                .artifactId(artifactId)
                .repositoryId(repoId)
                .comment(comment)
                .milestone(milestone)
                .file(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static ArtifactMilestoneEntity buildVersionEntity(
            final String versionId, final String artifactId, final String repoId, final String comment,
            final Integer milestone, final String file, final SaveTypeEnum saveTypeEnum) {
        return ArtifactMilestoneEntity.builder()
                .id(versionId)
                .artifactId(artifactId)
                .repositoryId(repoId)
                .comment(comment)
                .milestone(milestone)
                .file(file)
                .saveType(saveTypeEnum)
                .build();
    }

    public static ArtifactMilestoneUpload buildVersionUpload(final String comment, final String fileString, final SaveTypeEnum saveTypeEnum) {
        return ArtifactMilestoneUpload.builder()
                .comment(comment)
                .file(fileString)
                .saveType(saveTypeEnum)
                .build();
    }

    public static ArtifactMilestoneUpdate buildVersionUpdate(final String versionId, final String comment, final String fileString) {
        return ArtifactMilestoneUpdate.builder()
                .versionId(versionId)
                .comment(comment)
                .file(fileString)
                .build();
    }

}
