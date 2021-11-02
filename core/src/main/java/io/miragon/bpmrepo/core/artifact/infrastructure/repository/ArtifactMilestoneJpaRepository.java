package io.miragon.bpmrepo.core.artifact.infrastructure.repository;

import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactMilestoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtifactMilestoneJpaRepository extends JpaRepository<ArtifactMilestoneEntity, String> {

    Optional<ArtifactMilestoneEntity> findById(String milestoneId);

    List<ArtifactMilestoneEntity> findAllByArtifactId(String artifactId);

    List<ArtifactMilestoneEntity> findAllByDeployments_IdIn(List<String> deploymentIds);

    Optional<ArtifactMilestoneEntity> findFirstByArtifactIdOrderByMilestoneDesc(String artifactId);

    Optional<ArtifactMilestoneEntity> findFirstByArtifactIdAndMilestoneOrderByUpdatedDateDesc(String artifactId, Integer milestone);

    ArtifactMilestoneEntity findFirstByArtifactIdAndRepositoryIdOrderByMilestoneDesc(String artifactId, String repositoryId);

    int deleteAllByRepositoryId(String repositoryId);

    int deleteAllByArtifactId(String artifactId);


}
