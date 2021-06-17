package io.miragon.bpmnrepo.core.repository.domain.model;

import io.miragon.bpmnrepo.core.repository.api.transport.AssignmentTO;
import io.miragon.bpmnrepo.core.shared.enums.RoleEnum;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    private String userId;
    private String userName;
    private String bpmnRepositoryId;
    private RoleEnum roleEnum;

    public Assignment(final AssignmentTO assignmentTO){
        this.userId = assignmentTO.getUserId();
        this.userName = assignmentTO.getUserName();
        this.bpmnRepositoryId = assignmentTO.getBpmnRepositoryId();
        this.roleEnum = assignmentTO.getRoleEnum();
    }
}
