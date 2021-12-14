package io.miragon.bpmrepo.core.artifact.api.transport;

import io.miragon.bpmrepo.core.artifact.domain.enums.DeploymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private DeploymentStatus status;

    @Nullable
    private String message;

    @NotBlank
    private String user;

    @NotBlank
    private LocalDateTime timestamp;

}
