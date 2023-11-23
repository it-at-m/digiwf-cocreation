package de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Client created object containing the deployment target and artifact(-version) id(-s) for creating a new deployment")
public class NewDeploymentTO {

    @NotBlank
    private String repositoryId;

    @NotBlank
    private String artifactId;

    @NotBlank
    private String milestoneId;

    @NotBlank
    private String target;

}
