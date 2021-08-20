package io.miragon.bpmrepo.core.artifact.infrastructure.repository;

import io.miragon.bpmrepo.core.artifact.infrastructure.entity.SharedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharedJpaRepository extends JpaRepository<SharedEntity, String> {
    Optional<SharedEntity> findBySharedId_ArtifactIdAndSharedId_RepositoryId(final String artifactId, final String repositoryId);

    int deleteBySharedId_ArtifactIdAndSharedId_RepositoryId(final String artifactId, final String repositoryId);

    int deleteBySharedId_ArtifactIdAndSharedId_TeamId(final String artifactId, final String teamId);

    Optional<List<SharedEntity>> findBySharedId_ArtifactIdAndSharedId_RepositoryIdNotNull(final String artifactId);

    Optional<List<SharedEntity>> findBySharedId_RepositoryId(final String repositoryId);
}
