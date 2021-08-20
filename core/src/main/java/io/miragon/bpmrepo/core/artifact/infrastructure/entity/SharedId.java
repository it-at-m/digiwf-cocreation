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

    @Column(name = "artifact_id", nullable = false)
    private String artifactId;

    @Column(name = "repository_id")
    private String repositoryId;

    @Column(name = "team_id", nullable = true, columnDefinition = "int default 0")
    private String teamId;
}
