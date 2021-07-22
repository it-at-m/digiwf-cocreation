package io.miragon.bpmrepo.core.artifact.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Deployment {

    private final String id;

    private final String target;

    private final String user;

    private final LocalDateTime timestamp;

}
