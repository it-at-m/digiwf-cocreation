package io.miragon.bpmrepo.core.repository.domain.model;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Assignment {
    private final String userId;
    private final String username;
    private final String repositoryId;
    private final RoleEnum role;

    public Assignment(final AssignmentUpdate assignmentUpdate) {
        this.userId = assignmentUpdate.getUserId();
        this.username = assignmentUpdate.getUsername();
        this.repositoryId = assignmentUpdate.getRepositoryId();
        this.role = assignmentUpdate.getRole();
    }
}
