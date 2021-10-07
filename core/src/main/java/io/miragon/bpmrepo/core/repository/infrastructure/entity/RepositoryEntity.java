package io.miragon.bpmrepo.core.repository.infrastructure.entity;

import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "artifact_id")
    private List<ArtifactEntity> sharedArtifacts;
}
