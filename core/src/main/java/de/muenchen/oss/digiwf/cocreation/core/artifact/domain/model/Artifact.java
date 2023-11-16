package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Artifact {

    private String id;
    private String name;
    private String description;
    private String repositoryId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String fileType;
    private String lockedBy;
    private LocalDateTime lockedUntil;

    public void updateArtifact(final ArtifactUpdate artifactUpdate) {
        if (artifactUpdate.getName() != null && !artifactUpdate.getName().isEmpty()) {
            this.name = artifactUpdate.getName();
        }
        if (artifactUpdate.getDescription() != null && !artifactUpdate.getDescription().isEmpty()) {
            this.description = artifactUpdate.getDescription();
        }
        this.updateDate();
    }

    public void copyFrom(final Artifact artifact, final String repositoryId, final String title, final String description) {
        this.name = title;
        this.description = description;
        this.repositoryId = repositoryId;
        this.fileType = artifact.getFileType();
    }

    public void updateRepositoryId(final String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void updateDate() {
        this.updatedDate = LocalDateTime.now();
    }

    public void lock(final String lockedBy) {
        this.lockedBy = lockedBy;
        this.lockedUntil = LocalDateTime.now().plusMinutes(10);
    }

    public void unlock() {
        this.lockedBy = null;
        this.lockedUntil = LocalDateTime.now();
    }
}
