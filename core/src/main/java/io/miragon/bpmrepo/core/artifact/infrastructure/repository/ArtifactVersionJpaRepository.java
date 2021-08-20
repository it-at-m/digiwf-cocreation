package io.miragon.bpmrepo.core.artifact.infrastructure.repository;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtifactVersionJpaRepository extends JpaRepository<ArtifactVersionEntity, String> {

    Optional<ArtifactVersionEntity> findById(String versionId);

    List<ArtifactVersionEntity> findAllByArtifactId(String artifactId);

    Optional<ArtifactVersionEntity> findFirstByArtifactIdOrderByMilestoneDesc(String artifactId);

    ArtifactVersionEntity findFirstByArtifactIdAndRepositoryIdOrderByMilestoneDesc(String artifactId, String repositoryId);

    int deleteAllByRepositoryId(String repositoryId);

    int deleteAllByArtifactId(String artifactId);

    int deleteAllByRepositoryIdAndArtifactIdAndSaveType(String repositoryId, String artifactId, SaveTypeEnum saveTypeEnum);

    ArtifactVersionEntity save(ArtifactVersionEntity entity);
}
