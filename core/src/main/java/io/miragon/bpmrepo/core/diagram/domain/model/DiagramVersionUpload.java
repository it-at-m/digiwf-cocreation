package io.miragon.bpmrepo.core.diagram.domain.model;

import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiagramVersionUpload {
    
    private String versionComment;

    private String xml;

    private SaveTypeEnum saveType;
}
