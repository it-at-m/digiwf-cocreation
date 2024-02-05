package de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.repository;

import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.ArtifactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtifactJpaRepository extends JpaRepository<ArtifactEntity, String> {

    List<ArtifactEntity> findAllByRepositoryIdOrderByUpdatedDateDesc(String artifactRepositoryId);

    List<ArtifactEntity> findTop20ByRepositoryIdInOrderByUpdatedDateDesc(List<String> repositoryIds);

    int countAllByRepositoryId(String repositoryId);

    int deleteAllByRepositoryId(String repositoryId);

    List<ArtifactEntity> findAllByRepositoryIdInAndNameLikeIgnoreCase(List<String> repositoryIds, String title);

    List<ArtifactEntity> findAllByRepositoryIdAndFileTypeIgnoreCase(String repositoryId, String type);

    List<ArtifactEntity> findAllByIdIn(List<String> artifactIds);

    List<ArtifactEntity> findAllByIdInAndFileType(List<String> artifactIds, String type);

    List<ArtifactEntity> findAllByRepositoryIdIn(List<String> repositoryIds);

    ArtifactEntity getOne(String id);
}
