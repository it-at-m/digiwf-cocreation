package io.miragon.bpmrepo.core.artifact.infrastructure.entity;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
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
@Entity(name = "ArtifactVersion")
public class ArtifactVersionEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "version_id_", unique = true, nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "version_comment_")
    private String comment;

    @Column(name = "version_milestone_", nullable = false)
    private Integer milestone;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "version_save_type_", nullable = false)
    private SaveTypeEnum saveType;

    @Column(name = "version_updated_date_")
    private LocalDateTime updatedDate;

    @Column(name = "version_file_", columnDefinition = "TEXT")
    private String xml;

    @Column(name = "artifact_id_", nullable = false)
    private String artifactId;

    @Column(name = "repository_id_", nullable = false)
    private String repositoryId;

    @Column(name = "latest_version", nullable = false)
    private boolean latestVersion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "version_id_")
    private List<DeploymentEntity> deployments;
}
