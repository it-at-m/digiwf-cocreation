package io.miragon.bpmrepo.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Containing metadata for an artifact")
public class ArtifactTO {

    @NotBlank
    private String id;

    @NotNull
    private String repositoryId;

    @NotEmpty
    private String name;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private LocalDateTime updatedDate;

    @Nullable
    private String svgPreview;

    @NotNull
    private String fileType;

}
