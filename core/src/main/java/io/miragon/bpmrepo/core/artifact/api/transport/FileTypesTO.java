package io.miragon.bpmrepo.core.artifact.api.transport;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileTypesTO {

    private String name;

    private String svgIcon;
}
