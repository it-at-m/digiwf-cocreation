package io.miragon.bpmrepo.core.team.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Client created object for creating a new team")
public class NewTeamTO {
    @NotBlank
    private String name;

    @NotNull
    private String description;

}
