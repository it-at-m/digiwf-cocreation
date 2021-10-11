package io.miragon.bpmrepo.core.team.domain.model;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class TeamAssignment {
    private final String userId;
    private final String username;
    private final String teamId;
    private RoleEnum role;

    public void updateRole(final RoleEnum newRole) {
        this.role = newRole;
    }

}
