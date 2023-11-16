package de.muenchen.oss.digiwf.cocreation.core.version;

import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactMilestoneTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactMilestoneUpdateTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactMilestoneUploadTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactMilestone;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactMilestoneUpdate;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactMilestoneUpload;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.ArtifactMilestoneEntity;

public class MilestoneBuilder {

    public static ArtifactMilestone buildMilestone(final String versionId, final String artifactId, final String repoId, final String comment,
                                                   final Integer milestone,
                                                   final String file) {
        return ArtifactMilestone.builder()
                .id(versionId)
                .artifactId(artifactId)
                .repositoryId(repoId)
                .comment(comment)
                .milestone(milestone)
                .file(file)
                .build();
    }

    public static ArtifactMilestoneTO buildMilestoneTO(final String versionId, final String artifactId, final String repoId, final String comment,
                                                       final Integer milestone,
                                                       final String file) {
        return ArtifactMilestoneTO.builder()
                .id(versionId)
                .artifactId(artifactId)
                .repositoryId(repoId)
                .comment(comment)
                .milestone(milestone)
                .file(file)
                .build();
    }

    public static ArtifactMilestoneEntity buildMilestoneEntity(
            final String versionId, final String artifactId, final String repoId, final String comment,
            final Integer milestone, final String file) {
        return ArtifactMilestoneEntity.builder()
                .id(versionId)
                .artifactId(artifactId)
                .repositoryId(repoId)
                .comment(comment)
                .milestone(milestone)
                .file(file)
                .build();
    }

    public static ArtifactMilestoneUpload buildMilestoneUpload(final String comment, final String fileString) {
        return ArtifactMilestoneUpload.builder()
                .comment(comment)
                .file(fileString)
                .build();
    }

    public static ArtifactMilestoneUploadTO buildMilestoneUploadTO(final String comment, final String fileString) {
        return ArtifactMilestoneUploadTO.builder()
                .comment(comment)
                .file(fileString)
                .build();
    }

    public static ArtifactMilestoneUpdate buildMilestoneUpdate(final String versionId, final String comment, final String fileString) {
        return ArtifactMilestoneUpdate.builder()
                .milestoneId(versionId)
                .comment(comment)
                .file(fileString)
                .build();
    }

    public static ArtifactMilestoneUpdateTO buildMilestoneUpdateTO(final String milestoneId, final String comment, final String fileString) {
        return ArtifactMilestoneUpdateTO.builder()
                .milestoneId(milestoneId)
                .comment(comment)
                .file(fileString)
                .build();
    }

}
