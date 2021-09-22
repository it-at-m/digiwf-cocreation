package io.miragon.bpmrepo.core.sharing.api.transport;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Share-relation between an artifact an a team")
public class SharedTeamTO {

    @NotBlank
    private String artifactId;

    @NotBlank
    private String teamId;

    @NotBlank
    private RoleEnum role;

    @Nullable
    private String artifactName;

    @Nullable
    private String teamName;
}
