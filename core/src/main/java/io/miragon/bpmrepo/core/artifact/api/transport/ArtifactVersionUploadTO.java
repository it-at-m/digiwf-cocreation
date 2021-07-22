package io.miragon.bpmrepo.core.artifact.api.transport;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactVersionUploadTO {

    @Nullable
    private String versionComment;

    @NotNull
    private String xml;

    @Nullable
    private SaveTypeEnum saveType;

}
