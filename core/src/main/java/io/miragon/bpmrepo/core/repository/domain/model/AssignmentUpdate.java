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
public class AssignmentUpdate {

    private final String repositoryId;

    private final String userId;

    private final String username;

    private final RoleEnum role;

}
