package io.miragon.bpmrepo.core.repository.api.transport;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentTO {

    @NotEmpty
    private String repositoryId;

    @NotEmpty
    private String userId;

    @NotEmpty
    private String username;

    @NotEmpty
    private RoleEnum role;
}
