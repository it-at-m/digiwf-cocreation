package io.miragon.bpmrepo.core.team.infrastructure.entity;

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
public class TeamAssignmentId implements Serializable {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "team_id")
    private String teamId;
}
