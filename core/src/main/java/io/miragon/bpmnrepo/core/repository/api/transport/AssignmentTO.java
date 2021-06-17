package io.miragon.bpmnrepo.core.repository.api.transport;


import io.miragon.bpmnrepo.core.shared.enums.RoleEnum;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentTO {

    @NotEmpty
    private String bpmnRepositoryId;

    @NotEmpty
    private String userId;

    @NotEmpty
    private String userName;

    @NotEmpty
    private RoleEnum roleEnum;
}
