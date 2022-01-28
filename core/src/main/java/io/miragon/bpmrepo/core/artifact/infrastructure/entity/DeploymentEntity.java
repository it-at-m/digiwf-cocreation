package io.miragon.bpmrepo.core.artifact.infrastructure.entity;

import io.miragon.bpmrepo.core.artifact.domain.enums.DeploymentStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}
