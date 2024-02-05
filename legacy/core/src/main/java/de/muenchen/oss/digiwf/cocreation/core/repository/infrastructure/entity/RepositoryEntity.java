package de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity;

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
@Entity(name = "Repository_")
public class RepositoryEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "repository_id_", unique = true, nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "repository_name_")
    private String name;

    @Column(name = "repository_description_")
    private String description;

    @Column(name = "created_date_", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date_", nullable = false)
    private LocalDateTime updatedDate;

    @Column(name = "existing_artifacts_", columnDefinition = "integer default 0")
    private Integer existingArtifacts;

    @Column(name = "assigned_users_", columnDefinition = "integer default 1")
    private Integer assignedUsers;
}
