package io.miragon.bpmrepo.core.artifact.domain.model;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArtifactVersionUpload {

    private String versionComment;

    private String xml;

    private SaveTypeEnum saveType;
}
