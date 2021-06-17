package io.miragon.bpmnrepo.core.repository.api.transport;


import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDeletionTO {

    @NotEmpty
    private String bpmnRepositoryId;

    @NotEmpty
    private String userName;
}
