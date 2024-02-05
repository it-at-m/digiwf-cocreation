package de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "version of an artifact, contains the file and information about deployments")
public class ArtifactMilestoneTO {

    @NotNull
    private String id;

    @Nullable
    private String comment;

    @NotBlank
    private Integer milestone;

    @NotEmpty
    private String file;

    @NotEmpty
    private LocalDateTime updatedDate;

    @NotEmpty
    private String artifactId;

    @NotNull
    private boolean latestMilestone;

    @NotEmpty
    private String repositoryId;

    @NotNull
    private List<DeploymentTO> deployments;

}
