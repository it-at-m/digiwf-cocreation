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
public class RepoTeamAssignmentId implements Serializable {

    @Column(name = "repository_id")
    private String repositoryId;

    @Column(name = "team_id")
    private String teamId;
}
