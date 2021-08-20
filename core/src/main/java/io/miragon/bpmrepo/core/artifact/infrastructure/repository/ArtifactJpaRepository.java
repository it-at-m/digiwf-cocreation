package io.miragon.bpmrepo.core.artifact.infrastructure.repository;

import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtifactJpaRepository extends JpaRepository<ArtifactEntity, String> {

    Optional<List<ArtifactEntity>> findAllByRepositoryIdOrderByUpdatedDateDesc(String artifactRepositoryId);

    Optional<List<ArtifactEntity>> findTop10ByRepositoryIdInOrderByUpdatedDateDesc(List<String> repositoryIds);

    int countAllByRepositoryId(String repositoryId);

    int deleteAllByRepositoryId(String repositoryId);

    Optional<List<ArtifactEntity>> findAllByRepositoryIdInAndNameStartsWithIgnoreCase(List<String> repositoryIds, String title);

    Optional<List<ArtifactEntity>> findAllByRepositoryIdAndFileTypeIgnoreCase(String repositoryId, String type);

    Optional<List<ArtifactEntity>> findAllByIdIn(List<String> artifactIds);

    Optional<List<ArtifactEntity>> findAllByRepositoryIdIn(List<String> repositoryIds);

    ArtifactEntity getOne(String id);
}
