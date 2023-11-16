package de.muenchen.oss.digiwf.cocreation.core.repository;

import de.muenchen.oss.digiwf.cocreation.core.repository.api.transport.NewRepositoryTO;
import de.muenchen.oss.digiwf.cocreation.core.repository.api.transport.RepositoryTO;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.NewRepository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.Repository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.RepositoryUpdate;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.AssignmentEntity;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.AssignmentId;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.RepositoryEntity;
import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;

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

    public static NewRepositoryTO buildNewRepoTO(final String repoName, final String repoDesc) {
        return NewRepositoryTO.builder()
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
