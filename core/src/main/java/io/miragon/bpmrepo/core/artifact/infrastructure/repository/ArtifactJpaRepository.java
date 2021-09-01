package io.miragon.bpmrepo.core.artifact.infrastructure.repository;

import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtifactJpaRepository extends JpaRepository<ArtifactEntity, String> {

    List<ArtifactEntity> findAllByRepositoryIdOrderByUpdatedDateDesc(String artifactRepositoryId);

    List<ArtifactEntity> findTop10ByRepositoryIdInOrderByUpdatedDateDesc(List<String> repositoryIds);

    int countAllByRepositoryId(String repositoryId);

    int deleteAllByRepositoryId(String repositoryId);

    List<ArtifactEntity> findAllByRepositoryIdInAndNameStartsWithIgnoreCase(List<String> repositoryIds, String title);

    List<ArtifactEntity> findAllByRepositoryIdAndFileTypeIgnoreCase(String repositoryId, String type);

    List<ArtifactEntity> findAllByIdIn(List<String> artifactIds);

    List<ArtifactEntity> findAllByRepositoryIdIn(List<String> repositoryIds);

    ArtifactEntity getOne(String id);
}
