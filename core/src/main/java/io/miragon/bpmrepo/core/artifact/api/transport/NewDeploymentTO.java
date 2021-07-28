package io.miragon.bpmrepo.core.artifact.api.transport;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewDeploymentTO {

    @NotBlank
    private String target;

}
