package io.miragon.bpmrepo.core.artifact.api.transport;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewArtifactTO {

    @NotEmpty
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String fileType;

    @Nullable
    private String svgPreview;
}
