package io.miragon.bpmnrepo.core.repository.api.transport;


import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmnRepositoryTO {

    @Nullable
    private String bpmnRepositoryId;

    @NotEmpty
    private String bpmnRepositoryName;

    @NotNull
    private String bpmnRepositoryDescription;


}
