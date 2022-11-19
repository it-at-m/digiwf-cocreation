package io.miragon.bpmrepo.core.artifact;

import io.miragon.bpmrepo.core.artifact.api.transport.NewArtifactTO;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactUpdate;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactEntity;

import java.time.LocalDateTime;

public class ArtifactBuilder {

    public static Artifact buildArtifact(final String artifactId, final String repoId, final String artifactName,
                                         final String artifactDesc, final String fileType,
                                         final LocalDateTime createdDate, final LocalDateTime updatedDate) {
        return Artifact.builder()
                .id(artifactId)
                .repositoryId(repoId)
                .name(artifactName)
                .description(artifactDesc)
                .fileType(fileType)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    public static ArtifactEntity buildArtifactEntity(final String artifactId, final String repoId, final String artifactName, final String artifactDesc,
                                                     final LocalDateTime createdDate, final LocalDateTime updatedDate) {
        return ArtifactEntity.builder()
                .id(artifactId)
                .repositoryId(repoId)
                .name(artifactName)
                .description(artifactDesc)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    public static Artifact buildArtifact(final String artifactId, final String artifactName, final String artifactDesc) {
        return Artifact.builder()
                .id(artifactId)
                .name(artifactName)
                .description(artifactDesc)
                .build();
    }

    public static ArtifactUpdate buildArtifactUpdate(final String artifactName, final String artifactDesc) {
        return ArtifactUpdate.builder()
                .name(artifactName)
                .description(artifactDesc)
                .build();
    }

    public static NewArtifactTO buildNewArtifactTO(final String name, final String description, final String fileType) {
        return NewArtifactTO.builder()
                .name(name)
                .description(description)
                .fileType(fileType)
                .build();
    }

}
