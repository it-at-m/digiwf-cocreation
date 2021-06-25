package io.miragon.bpmrepo.core.diagram.infrastructure.repository;

import io.miragon.bpmrepo.core.diagram.infrastructure.entity.DiagramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagramJpaRepository extends JpaRepository<DiagramEntity, String> {

    List<DiagramEntity> findAllByRepositoryId(String bpmnDiagramRepositoryId);

    int countAllByRepositoryId(String bpmnRepositoryId);

    int deleteAllByRepositoryId(String bpmnRepositoryId);

    List<DiagramEntity> findAllByRepositoryIdInAndNameStartsWith(List<String> bpmnRepositoryIds, String titel);
}
