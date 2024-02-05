package de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Client created object for uploading a version from the client")
public class ArtifactMilestoneUploadTO {

    @Nullable
    private String comment;

    @NotNull
    private String file;


}
