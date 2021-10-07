package io.miragon.bpmrepo.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Containing information about the deployment of an artifact")
public class DeploymentTO {

    @NotBlank
    private String repositoryId;

    @NotBlank
    private String artifactId;

    @NotBlank
    private String id;

    @NotBlank
    private String target;

    @NotBlank
    private String user;

    @NotBlank
    private LocalDateTime timestamp;

}
