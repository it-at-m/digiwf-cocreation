package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArtifactMilestoneUpdate {

    private String milestoneId;

    private String comment;

    private String file;
}
