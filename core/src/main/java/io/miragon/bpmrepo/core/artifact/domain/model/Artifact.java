package io.miragon.bpmrepo.core.artifact.domain.model;

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
    private String svgPreview;
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

    public void copy(final Artifact artifact) {
        this.name = artifact.getName();
        this.description = artifact.getDescription();
        this.fileType = artifact.getFileType();
    }

    public void updateRepositoryId(final String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void updateSvgPreview(final String svgPreview) {
        this.svgPreview = svgPreview;
    }

    public void updateDate() {
        this.updatedDate = LocalDateTime.now();
    }

    public void lock(final String lockedBy) {
        this.lockedBy = lockedBy;
        this.lockedUntil = LocalDateTime.now().plusMinutes(1);
    }

    public void unlock() {
        this.lockedBy = null;
        this.lockedUntil = LocalDateTime.now();
    }
}
