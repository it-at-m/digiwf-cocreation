package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Starred {
    private String artifactId;
    private String userId;

}
