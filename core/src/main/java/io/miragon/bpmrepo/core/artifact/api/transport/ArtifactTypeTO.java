package io.miragon.bpmrepo.core.artifact.api.transport;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactTypeTO {

    @NotBlank
    private String name;

    @NotBlank
    private String fileExtension;

    @NotBlank
    private String svgIcon;

    @NotBlank
    private String url;
}
