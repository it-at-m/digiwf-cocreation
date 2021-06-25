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
public class AssignmentWithUserNameTO {

    @NotEmpty
    private String bpmnRepositoryId;

    @NotEmpty
    private String userName;

    @NotNull
    private RoleEnum roleEnum;
}
