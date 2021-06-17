package io.miragon.bpmnrepo.core.diagram.domain.model;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BpmnDiagramVersionUpload {
    private String bpmnDiagramVersionFile;
    private String bpmnDiagramVersionNumber;
}
