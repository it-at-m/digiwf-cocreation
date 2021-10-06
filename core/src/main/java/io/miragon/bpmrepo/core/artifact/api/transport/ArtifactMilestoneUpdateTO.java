package io.miragon.bpmrepo.core.artifact.api.transport;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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