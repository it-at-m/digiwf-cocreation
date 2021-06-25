package io.miragon.bpmrepo.core.diagram.api.transport;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewDiagramTO {

    @NotEmpty
    private String name;

    @NotNull
    private String description;

    @Nullable
    private String fileType;

    @Nullable
    private String svgPreview;
}
