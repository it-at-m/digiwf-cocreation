package io.miragon.bpmrepo.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Transports an svg image as preview for an artifact")
public class ArtifactSVGUploadTO {

    @NotEmpty
    private String svgPreview;
}
