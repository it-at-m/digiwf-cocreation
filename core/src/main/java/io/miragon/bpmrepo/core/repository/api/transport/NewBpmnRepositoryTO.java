package io.miragon.bpmrepo.core.repository.api.transport;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewBpmnRepositoryTO {

    @NotBlank
    private String bpmnRepositoryName;

    @NotNull
    private String bpmnRepositoryDescription;

}
