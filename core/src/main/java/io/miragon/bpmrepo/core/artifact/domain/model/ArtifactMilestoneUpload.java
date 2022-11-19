package io.miragon.bpmrepo.core.artifact.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArtifactMilestoneUpload {

    private String comment;

    private String file;
}
