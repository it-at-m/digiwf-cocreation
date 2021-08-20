package io.miragon.bpmrepo.core.repository.api.transport;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
