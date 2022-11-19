package io.miragon.bpmrepo.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Client created object for updating accessible properties of an artifact")
public class ArtifactUpdateTO {

    @NotEmpty
    private String name;

    @NotNull
    private String description;

    @Nullable
    private String fileType;

    @Nullable
    private String file;

}
