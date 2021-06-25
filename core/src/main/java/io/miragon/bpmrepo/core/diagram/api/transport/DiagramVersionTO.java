package io.miragon.bpmrepo.core.diagram.api.transport;

import com.sun.istack.Nullable;
import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagramVersionTO {

    @Nullable
    private String id;

    @Nullable
    private String comment;

    @Nullable
    private Integer release;

    @Nullable
    private Integer milestone;

    @NotEmpty
    private String xml;

    @NotEmpty
    private SaveTypeEnum saveType;

    @NotEmpty
    private LocalDateTime updatedDate;

    @NotEmpty
    private String diagramId;

    @NotEmpty
    private String repositoryId;

}
