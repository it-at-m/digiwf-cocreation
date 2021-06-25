package io.miragon.bpmrepo.core.assignment;

import io.miragon.bpmrepo.core.repository.api.transport.AssignmentDeletionTO;
import io.miragon.bpmrepo.core.repository.api.transport.AssignmentTO;
import io.miragon.bpmrepo.core.repository.api.transport.AssignmentWithUserNameTO;
import io.miragon.bpmrepo.core.repository.domain.model.Assignment;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentId;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;

public class AssignmentBuilder {

    public static AssignmentEntity buildAssignmentEntity(final AssignmentId assignmentId, final RoleEnum roleEnum) {
        return AssignmentEntity.builder()
                .assignmentId(assignmentId)
                .roleEnum(roleEnum)
                .build();
    }

    public static Assignment buildAssignment(final String userId, final String repoId, final RoleEnum roleEnum) {
        return Assignment.builder()
                .userId(userId)
                .bpmnRepositoryId(repoId)
                .roleEnum(roleEnum)
                .build();
    }

    public static AssignmentTO buildAssignmentTO(final String repoId, final String usserId, final RoleEnum role) {
        return AssignmentTO.builder()
                .bpmnRepositoryId(repoId)
                .userId(usserId)
                .roleEnum(role)
                .build();
    }

    public static AssignmentId buildAssignmentId(final String userId, final String bpmnRepositoryId) {
        return AssignmentId.builder()
                .userId(userId)
                .bpmnRepositoryId(bpmnRepositoryId)
                .build();
    }

    public static AssignmentDeletionTO buildAssignmentDeletion(final String repoId, final String userName) {
        return AssignmentDeletionTO.builder()
                .bpmnRepositoryId(repoId)
                .userName(userName)
                .build();
    }

    public static AssignmentWithUserNameTO buildAssignmentWithUserName(final String repoId, final String username, final RoleEnum role) {
        return AssignmentWithUserNameTO.builder()
                .bpmnRepositoryId(repoId)
                .userName(username)
                .roleEnum(role)
                .build();
    }

}
