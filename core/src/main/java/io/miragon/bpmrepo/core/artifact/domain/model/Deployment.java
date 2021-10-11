package io.miragon.bpmrepo.core.artifact.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Deployment {

    private final String repositoryId;

    private final String artifactId;

    private final String id;

    private final String target;

    private String user;

    private LocalDateTime timestamp;

}
