package io.miragon.bpmrepo.core.artifact.api.transport;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileTypesTO {

    @NotBlank
    private String name;

    @NotBlank
    private String svgIcon;

    @NotBlank
    private String url;
}
