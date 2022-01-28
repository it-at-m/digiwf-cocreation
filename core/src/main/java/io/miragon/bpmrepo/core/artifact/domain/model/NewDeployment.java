package io.miragon.bpmrepo.core.artifact.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class NewDeployment {

    private final String repositoryId;

    private final String artifactId;

    private final String milestoneId;

    private final String target;

}
