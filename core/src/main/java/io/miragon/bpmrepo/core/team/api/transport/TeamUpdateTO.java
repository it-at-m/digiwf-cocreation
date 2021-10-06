package io.miragon.bpmrepo.core.team.api.transport;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Containing information about a team update")
public class TeamUpdateTO {
    @NotEmpty
    private String name;

    @NotNull
    private String description;

}
