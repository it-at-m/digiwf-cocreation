package io.miragon.bpmrepo.core.team.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Team {
    private String id;
    private String name;
    private String description;
    private Integer assignedUsers;

    public Team(final NewTeam newTeam) {
        this.name = newTeam.getName();
        this.description = newTeam.getDescription();
        this.assignedUsers = 0;
    }

    public void updateTeam(final TeamUpdate teamUpdate) {
        this.name = teamUpdate.getName();
        this.description = teamUpdate.getDescription();
    }

    public void updateAssignedUsers(final Integer assignedUsers) {
        this.assignedUsers = assignedUsers;
    }
}
