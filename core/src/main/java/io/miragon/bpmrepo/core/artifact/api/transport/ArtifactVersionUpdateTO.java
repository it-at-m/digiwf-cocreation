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
public class ArtifactVersionUpdateTO {

    @NotNull
    private String versionId;

    @NotNull
    private String xml;

    @Nullable
    private String versionComment;

    @Nullable
    private SaveTypeEnum saveType;

}