package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArtifactUpdate {

    private String name;
    private String description;
    private String fileType;
    private String file;

}
