package io.miragon.bpmnrepo.core.diagram.api.transport;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BpmnDiagramVersionUploadTO {

    @Nullable
    private String bpmnDiagramVersionComment;

    @NotNull
    private String bpmnAsXML;

    @Nullable
    private SaveTypeEnum saveType;



}
