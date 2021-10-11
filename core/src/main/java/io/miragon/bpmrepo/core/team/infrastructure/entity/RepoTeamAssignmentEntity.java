package io.miragon.bpmrepo.core.team.infrastructure.entity;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Repo_Team_Assignment_")
public class RepoTeamAssignmentEntity {

    @EmbeddedId
    private RepoTeamAssignmentId repoTeamAssignmentId;


    //0: OWNER - 1:ADMIN - 2:MEMBER - 3:VIEWER
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role_")
    private RoleEnum role;
}
