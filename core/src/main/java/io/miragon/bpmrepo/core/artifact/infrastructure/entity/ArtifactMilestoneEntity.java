package io.miragon.bpmrepo.core.artifact.infrastructure.entity;

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
@Entity(name = "Artifact_Milestone_")
public class ArtifactMilestoneEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "milestone_id_", unique = true, nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "comment_")
    private String comment;

    @Column(name = "milestone_", nullable = false)
    private Integer milestone;

    @Column(name = "updated_date_")
    private LocalDateTime updatedDate;

    @Column(name = "file_", columnDefinition = "TEXT")
    private String file;

    @Column(name = "id_", nullable = false)
    private String artifactId;

    @Column(name = "repository_id_", nullable = false)
    private String repositoryId;

    @Column(name = "latest_milestone", nullable = false)
    private boolean latestMilestone;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "milestone_id")
    private List<DeploymentEntity> deployments;
}
