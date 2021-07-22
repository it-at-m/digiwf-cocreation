package io.miragon.bpmrepo.core.artifact.infrastructure.repository;

import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtifactJpaRepository extends JpaRepository<ArtifactEntity, String> {

    List<ArtifactEntity> findAllByRepositoryId(String bpmnArtifactRepositoryId);

    int countAllByRepositoryId(String bpmnRepositoryId);

    int deleteAllByRepositoryId(String bpmnRepositoryId);

    List<ArtifactEntity> findAllByRepositoryIdInAndNameStartsWithIgnoreCase(List<String> bpmnRepositoryIds, String titel);
}
