package io.miragon.bpmrepo.core.repository.api.transport;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDeletionTO {

    @NotEmpty
    private String repositoryId;

    @NotEmpty
    private String userName;
}
