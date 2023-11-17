package de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Client created object for creating a new artifact")
public class NewArtifactTO {

    @NotEmpty
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String fileType;

    @Nullable
    private String file;

}
