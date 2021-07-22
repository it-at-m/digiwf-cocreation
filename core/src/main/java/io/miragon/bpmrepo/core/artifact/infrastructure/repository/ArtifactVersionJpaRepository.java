package io.miragon.bpmrepo.core.artifact.infrastructure.repository;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtifactVersionJpaRepository extends JpaRepository<ArtifactVersionEntity, String> {

    Optional<ArtifactVersionEntity> findById(String versionId);

    List<ArtifactVersionEntity> findAllByArtifactId(String bpmnartifactId);

    Optional<ArtifactVersionEntity> findFirstByArtifactIdOrderByMilestoneDesc(String bpmnartifactId);

    ArtifactVersionEntity findFirstByArtifactIdAndRepositoryIdOrderByMilestoneDesc(String bpmnartifactId, String bpmnRepositoryId);

    int deleteAllByRepositoryId(String bpmnRepositoryId);

    int deleteAllByArtifactId(String bpmnartifactId);

    int deleteAllByRepositoryIdAndArtifactIdAndSaveType(String bpmnRepositoryId, String bpmnartifactId, SaveTypeEnum saveTypeEnum);

    ArtifactVersionEntity save(ArtifactVersionEntity entity);
}
