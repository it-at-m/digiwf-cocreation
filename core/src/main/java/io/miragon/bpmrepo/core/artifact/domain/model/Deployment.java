package io.miragon.bpmrepo.core.artifact.domain.model;

import io.miragon.bpmrepo.core.artifact.domain.enums.DeploymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Deployment {

    private final String id;

    private final String repositoryId;

    private final String artifactId;

    private final String target;

    private DeploymentStatus status;

    private String message;

    private final String user;

    private final LocalDateTime timestamp;

    public Deployment(final NewDeployment newDeployment, final String user) {
        this.id = newDeployment.getId();
        this.repositoryId = newDeployment.getRepositoryId();
        this.artifactId = newDeployment.getArtifactId();
        this.target = newDeployment.getTarget();
        this.timestamp = LocalDateTime.now();
        this.user = user;
    }

}
