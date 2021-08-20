package io.miragon.bpmrepo.core.repository;

import io.miragon.bpmrepo.core.repository.api.transport.RepositoryTO;
import io.miragon.bpmrepo.core.repository.domain.model.NewRepository;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.model.RepositoryUpdate;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentId;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.RepositoryEntity;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;

import java.time.LocalDateTime;

public class RepositoryBuilder {

    public static Repository buildRepo(final String repoId, final String repoName, final String repoDesc, final LocalDateTime createdDate,
                                       final LocalDateTime updatedDate) {
        return Repository.builder()
                .id(repoId)
                .name(repoName)
                .description(repoDesc)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    public static NewRepository buildNewRepo(final String repoName, final String repoDesc) {
        return NewRepository.builder()
                .name(repoName)
                .description(repoDesc)
                .build();
    }

    public static RepositoryUpdate buildRepoUpdate(final String repoName, final String repoDesc) {
        return RepositoryUpdate.builder()
                .name(repoName)
                .description(repoDesc)
                .build();
    }

    public static RepositoryTO buildNewRepoRequestTO(final String repoId, final String repoName, final String repoDesc,
                                                     final Integer existingArtifacts, final Integer assignedUsers) {
        return RepositoryTO.builder()
                .id(repoId)
                .name(repoName)
                .description(repoDesc)
                .existingArtifacts(existingArtifacts)
                .assignedUsers(assignedUsers)
                .build();
    }

    public static RepositoryTO buildRepoTO(final String repoId, final String repoName, final String repoDesc) {
        return RepositoryTO.builder()
                .id(repoId)
                .name(repoName)
                .description(repoDesc)
                .build();
    }

    public static RepositoryEntity buildRepoEntity(final String repoId, final String repoName, final String repoDesc, final LocalDateTime createdDate,
                                                   final LocalDateTime updatedDate) {
        return RepositoryEntity.builder()
                .id(repoId)
                .name(repoName)
                .description(repoDesc)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }

    public static AssignmentEntity buildAssignment(final String userId, final String repositoryId, final RoleEnum role) {
        final AssignmentId assignmentId = AssignmentId.builder()
                .userId(userId)
                .repositoryId(repositoryId)
                .build();

        return AssignmentEntity.builder()
                .assignmentId(assignmentId)
                .role(role)
                .build();
    }

}
