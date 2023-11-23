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
@Schema(description = "Client created Object for updating accessible properties of a version")
public class ArtifactMilestoneUpdateTO {

    @NotNull
    private String milestoneId;

    @NotNull
    private String file;

    @Nullable
    private String comment;

}
