package io.miragon.bpmrepo.core.diagram.domain.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiagramUpdate {

    private String name;
    private String description;
    private String svgPreview;
    private String fileType;

}
