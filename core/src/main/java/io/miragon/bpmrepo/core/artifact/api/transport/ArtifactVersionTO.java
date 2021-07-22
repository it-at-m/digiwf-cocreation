package io.miragon.bpmrepo.core.artifact.api.transport;

import com.sun.istack.Nullable;
import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactVersionTO {

    @Nullable
    private String id;

    @Nullable
    private String comment;

    @Nullable
    private Integer milestone;

    @NotEmpty
    private String xml;

    @NotEmpty
    private SaveTypeEnum saveType;

    @NotEmpty
    private LocalDateTime updatedDate;

    @NotEmpty
    private String artifactId;

    @NotEmpty
    private String repositoryId;

    @Nullable
    private List<Deployment> deployments = new ArrayList<>();


}
