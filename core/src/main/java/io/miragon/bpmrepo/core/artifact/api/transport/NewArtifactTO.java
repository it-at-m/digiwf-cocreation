package io.miragon.bpmrepo.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Client created object for creating a new artifact")
public class NewArtifactTO {

    @NotEmpty
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String fileType;

}
