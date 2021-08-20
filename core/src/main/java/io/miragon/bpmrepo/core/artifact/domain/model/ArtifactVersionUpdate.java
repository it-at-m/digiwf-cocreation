package io.miragon.bpmrepo.core.artifact.domain.model;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArtifactVersionUpdate {

    private String versionId;

    private String comment;

    private String xml;

    private SaveTypeEnum saveType;
}
