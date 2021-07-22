package io.miragon.bpmrepo.core.artifact.api.transport;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactSVGUploadTO {

    @NotEmpty
    private String svgPreview;
}
