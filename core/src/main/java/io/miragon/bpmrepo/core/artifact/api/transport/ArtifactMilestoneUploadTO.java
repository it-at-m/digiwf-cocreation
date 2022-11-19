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
@Schema(description = "Client created object for uploading a version from the client")
public class ArtifactMilestoneUploadTO {

    @Nullable
    private String comment;

    @NotNull
    private String file;


}
