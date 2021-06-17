package io.miragon.bpmnrepo.core.diagram.api.transport;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BpmnDiagramSVGUploadTO {
    @NotEmpty
    private String svgPreview;
}
