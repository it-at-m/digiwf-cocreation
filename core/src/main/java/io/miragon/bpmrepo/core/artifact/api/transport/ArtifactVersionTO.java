package io.miragon.bpmrepo.core.artifact.api.transport;

import com.sun.istack.Nullable;
import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactVersionTO {

    @NotNull
    private String id;

    @Nullable
    private String comment;

    @NotBlank
    private Integer milestone;

    @NotEmpty
    private String xml;

    @NotEmpty
    private SaveTypeEnum saveType;

    @NotEmpty
    private LocalDateTime updatedDate;

    @NotEmpty
    private String artifactId;

    @NotNull
    private boolean latestVersion;

    @NotEmpty
    private String repositoryId;

    @NotNull
    private List<DeploymentTO> deployments = new ArrayList<>();

}
