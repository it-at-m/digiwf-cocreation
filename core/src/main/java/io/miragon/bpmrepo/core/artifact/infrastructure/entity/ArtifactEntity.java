package io.miragon.bpmrepo.core.artifact.infrastructure.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Artifact")
public class ArtifactEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "artifact_id", unique = true, nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "repository_id")
    private String repositoryId;

    @Column(name = "artifact_name")
    private String name;

    @Column(name = "artifact_description")
    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "svg_preview", columnDefinition = "TEXT")
    private String svgPreview;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "locked_by")
    private String lockedBy;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;
}
