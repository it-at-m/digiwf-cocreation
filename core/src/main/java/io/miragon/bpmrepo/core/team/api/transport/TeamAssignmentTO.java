package io.miragon.bpmrepo.core.team.api.transport;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Containing information about a user-team relation")
public class TeamAssignmentTO {

    @NotEmpty
    private String teamId;

    @NotEmpty
    private String userId;

    @NotEmpty
    private RoleEnum role;
}
