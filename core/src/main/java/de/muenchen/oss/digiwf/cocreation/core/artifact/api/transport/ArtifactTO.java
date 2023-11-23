package de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

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

    @NotNull
    private String fileType;

    @Nullable
    private String lockedBy;

    @Nullable
    private LocalDateTime lockedUntil;

}
