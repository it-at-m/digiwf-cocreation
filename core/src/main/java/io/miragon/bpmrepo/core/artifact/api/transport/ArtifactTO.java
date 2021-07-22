package io.miragon.bpmrepo.core.artifact.api.transport;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactTO {

    @Nullable
    private String id;

    @NotNull
    private String repositoryId;

    @NotEmpty
    private String name;

    @NotNull
    private String description;

    @Nullable
    private LocalDateTime createdDate;

    @Nullable
    private LocalDateTime updatedDate;

    @Nullable
    private String svgPreview;

    @NotNull
    private String fileType;

}
