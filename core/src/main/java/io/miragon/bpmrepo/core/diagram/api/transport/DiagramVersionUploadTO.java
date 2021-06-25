package io.miragon.bpmrepo.core.diagram.api.transport;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagramVersionUploadTO {

    @Nullable
    private String versionComment;

    @NotNull
    private String xml;

    @Nullable
    private SaveTypeEnum saveType;

}
