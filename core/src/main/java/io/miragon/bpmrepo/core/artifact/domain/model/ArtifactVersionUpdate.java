package io.miragon.bpmrepo.core.artifact.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArtifactVersionUpdate {

    private String versionId;

    private String comment;

    private String file;
}
