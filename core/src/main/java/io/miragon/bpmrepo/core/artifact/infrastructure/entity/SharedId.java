package io.miragon.bpmrepo.core.artifact.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SharedId implements Serializable {

    @Column(name = "artifact_id_", nullable = false)
    private String artifactId;

    @Column(name = "repository_id_")
    private String repositoryId;

    @Column(name = "team_id_", nullable = true)
    private String teamId;
}
