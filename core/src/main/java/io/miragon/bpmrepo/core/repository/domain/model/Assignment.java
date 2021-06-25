package io.miragon.bpmrepo.core.repository.domain.model;

import io.miragon.bpmrepo.core.repository.api.transport.AssignmentTO;
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
    private final String userName;
    private final String bpmnRepositoryId;
    private final RoleEnum roleEnum;

    public Assignment(final AssignmentTO assignmentTO) {
        this.userId = assignmentTO.getUserId();
        this.userName = assignmentTO.getUserName();
        this.bpmnRepositoryId = assignmentTO.getBpmnRepositoryId();
        this.roleEnum = assignmentTO.getRoleEnum();
    }
}
