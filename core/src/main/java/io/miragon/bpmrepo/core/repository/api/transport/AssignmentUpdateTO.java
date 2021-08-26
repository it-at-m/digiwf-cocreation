package io.miragon.bpmrepo.core.repository.api.transport;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Client created object for changing the role of an user in a repository")
public class AssignmentUpdateTO {

    @NotEmpty
    private String repositoryId;

    @NotEmpty
    private String username;

    @NotEmpty
    private String userId;

    @NotNull
    private RoleEnum role;
}
