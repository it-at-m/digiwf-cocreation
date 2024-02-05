package de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Artifact_")
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


    @Column(name = "file_type_")
    private String fileType;

    @Column(name = "locked_by_")
    private String lockedBy;

    @Column(name = "locked_until_")
    private LocalDateTime lockedUntil;
}
