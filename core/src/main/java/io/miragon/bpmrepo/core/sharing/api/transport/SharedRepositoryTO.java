package io.miragon.bpmrepo.core.sharing.api.transport;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SharedRepositoryTO {

    @NotBlank
    private String artifactId;

    @NotBlank
    private String repositoryId;

    @NotNull
    private RoleEnum role;

    @Nullable
    private String artifactName;

    @Nullable
    private String repositoryName;


}
