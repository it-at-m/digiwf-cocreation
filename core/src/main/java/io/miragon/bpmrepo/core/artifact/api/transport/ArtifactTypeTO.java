package io.miragon.bpmrepo.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Specifies properties for supported file types")
public class ArtifactTypeTO {

    @NotBlank
    private String name;

    @NotBlank
    private String fileExtension;

    @NotBlank
    private String svgIcon;

    private String url;

    // does a suitable editor exist for the artifact type
    private boolean editable;

    private boolean deployable;
}
