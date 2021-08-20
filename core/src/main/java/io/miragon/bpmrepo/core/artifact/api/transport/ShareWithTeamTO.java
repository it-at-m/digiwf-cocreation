package io.miragon.bpmrepo.core.artifact.api.transport;


import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareWithTeamTO {

    @NotBlank
    private String artifactId;

    @NotBlank
    private String teamId;

    @NotBlank
    private RoleEnum role;
}
