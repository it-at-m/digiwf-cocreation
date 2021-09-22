package io.miragon.bpmrepo.core.sharing.infrastructure.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ShareWithTeamId implements Serializable {

    @Column(name = "artifact_id_", nullable = false)
    private String artifactId;

    @Column(name = "team_id_", nullable = false)
    private String teamId;
}
