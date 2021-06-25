package io.miragon.bpmrepo.core.repository;

import io.miragon.bpmrepo.core.repository.api.transport.BpmnRepositoryRequestTO;
import io.miragon.bpmrepo.core.repository.api.transport.BpmnRepositoryTO;
import io.miragon.bpmrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmrepo.core.repository.domain.model.BpmnRepository;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentId;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.BpmnRepositoryEntity;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;

import java.time.LocalDateTime;

public class RepositoryBuilder {

    public static BpmnRepository buildRepo(final String repoId, final String repoName, final String repoDesc, final LocalDateTime createdDate,
            final LocalDateTime updatedDate) {
        return BpmnRepository.builder()
                .bpmnRepositoryId(repoId)
                .bpmnRepositoryName(repoName)
                .bpmnRepositoryDescription(repoDesc)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    public static NewBpmnRepositoryTO buildNewRepoTO(final String repoName, final String repoDesc) {
        return NewBpmnRepositoryTO.builder()
                .bpmnRepositoryName(repoName)
                .bpmnRepositoryDescription(repoDesc)
                .build();
    }

    public static BpmnRepositoryRequestTO buildNewRepoRequestTO(final String repoId, final String repoName, final String repoDesc,
            final Integer existingDiagrams, final Integer assignedUsers) {
        return BpmnRepositoryRequestTO.builder()
                .bpmnRepositoryId(repoId)
                .bpmnRepositoryName(repoName)
                .bpmnRepositoryDescription(repoDesc)
                .existingDiagrams(existingDiagrams)
                .assignedUsers(assignedUsers)
                .build();
    }

    public static BpmnRepositoryTO buildRepoTO(final String repoId, final String repoName, final String repoDesc) {
        return BpmnRepositoryTO.builder()
                .bpmnRepositoryId(repoId)
                .bpmnRepositoryName(repoName)
                .bpmnRepositoryDescription(repoDesc)
                .build();
    }

    public static BpmnRepositoryEntity buildRepoEntity(final String repoId, final String repoName, final String repoDesc, final LocalDateTime createdDate,
            final LocalDateTime updatedDate) {
        return BpmnRepositoryEntity.builder()
                .bpmnRepositoryId(repoId)
                .bpmnRepositoryName(repoName)
                .bpmnRepositoryDescription(repoDesc)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    public static AssignmentEntity buildAssignment(final String userId, final String bpmnRepositoryId, final RoleEnum roleEnum) {
        final AssignmentId assignmentId = AssignmentId.builder()
                .userId(userId)
                .bpmnRepositoryId(bpmnRepositoryId)
                .build();

        return AssignmentEntity.builder()
                .assignmentId(assignmentId)
                .roleEnum(roleEnum)
                .build();
    }

}
