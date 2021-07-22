package io.miragon.bpmrepo.core.artifact.domain.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArtifactUpdate {

    private String name;
    private String description;
    private String svgPreview;
    private String fileType;

}
