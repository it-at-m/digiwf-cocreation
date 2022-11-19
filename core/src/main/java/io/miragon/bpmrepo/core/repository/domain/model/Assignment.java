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
    private final String repositoryId;
    private final RoleEnum role;

}
