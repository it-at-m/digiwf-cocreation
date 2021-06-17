package io.miragon.bpmnrepo.core.repository.api.transport;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmnRepositoryRequestTO {
    @NotEmpty
    private String bpmnRepositoryId;

    @NotEmpty
    private String bpmnRepositoryName;

    @NotNull
    private String bpmnRepositoryDescription;

    @NotNull
    private Integer existingDiagrams;

    @NotNull
    private Integer assignedUsers;
}
