package io.miragon.bpmrepo.core.artifact.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class NewDeployment {

    private String id;

    private final String repositoryId;

    private final String artifactId;

    private final String milestoneId;

    private final String target;

    public String generateId() {
        this.id = UUID.randomUUID().toString();
        return this.id;
    }
}
