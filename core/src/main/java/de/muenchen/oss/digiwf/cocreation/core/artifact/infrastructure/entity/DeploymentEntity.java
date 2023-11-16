package de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity;

import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.enums.DeploymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity(name = "Deployment_")
public class DeploymentEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "deployment_id_", unique = true, nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "repository_id_")
    private String repositoryId;

    @Column(name = "artifact_id_")
    private String artifactId;

    @Column(name = "target_")
    private String target;

    @Column(name = "user_")
    private String user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_")
    private DeploymentStatus status = DeploymentStatus.PENDING;

    @Column(name = "message_")
    private String message;

    @Column(name = "timestamp_")
    private LocalDateTime timestamp;

    public DeploymentEntity(final String id, final String repositoryId, final String artifactId, final String target, final String user, final DeploymentStatus status, final String message, final LocalDateTime timestamp) {
        this.id = id;
        this.repositoryId = repositoryId;
        this.artifactId = artifactId;
        this.target = target;
        this.user = user;
        if (status != null) {
            this.status = status;
        }
        this.message = message;
        this.timestamp = timestamp;
    }
}
