package io.miragon.bpmrepo.core.artifact.domain.model;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShareWithTeam {
    private String artifactId;
    private String teamId;
    private RoleEnum role;
}
