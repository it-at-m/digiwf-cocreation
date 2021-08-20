package io.miragon.bpmrepo.core.artifact.api.transport;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareWithRepositoryTO {

    @NotBlank
    private String artifactId;

    @NotBlank
    private String repositoryId;

    @NotBlank
    private RoleEnum role;
}
