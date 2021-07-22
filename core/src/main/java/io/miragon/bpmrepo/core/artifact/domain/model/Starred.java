package io.miragon.bpmrepo.core.artifact.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Starred {
    private String bpmnartifactId;
    private String userId;

}
