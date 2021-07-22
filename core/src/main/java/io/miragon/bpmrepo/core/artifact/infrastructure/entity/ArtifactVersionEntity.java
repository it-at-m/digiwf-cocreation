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
    @Column(name = "version_id", unique = true, nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "version_comment")
    private String comment;

    @Column(name = "version_milestone", nullable = false)
    private Integer milestone;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "version_save_type", nullable = false)
    private SaveTypeEnum saveType;

    @Column(name = "version_updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "version_file", columnDefinition = "TEXT")
    private String xml;

    @Column(name = "artifact_id", nullable = false)
    private String artifactId;

    @Column(name = "bpmn_repository_id", nullable = false)
    private String repositoryId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "version_id")
    private List<DeploymentEntity> deployments;

}
