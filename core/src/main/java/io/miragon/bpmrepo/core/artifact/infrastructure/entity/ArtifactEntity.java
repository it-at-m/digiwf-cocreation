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
    @Column(name = "artifact_id_", unique = true, nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "repository_id_")
    private String repositoryId;

    @Column(name = "name_")
    private String name;

    @Column(name = "description_")
    private String description;

    @Column(name = "created_date_")
    private LocalDateTime createdDate;

    @Column(name = "updated_date_")
    private LocalDateTime updatedDate;

    @Column(name = "svg_preview_", columnDefinition = "TEXT")
    private String svgPreview;

    @Column(name = "file_type_")
    private String fileType;

    @Column(name = "locked_by_")
    private String lockedBy;

    @Column(name = "locked_until_")
    private LocalDateTime lockedUntil;
}
