package io.miragon.bpmnrepo.core.diagram.infrastructure.repository;

import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BpmnDiagramJpa extends JpaRepository<BpmnDiagramEntity, String> {

    List<BpmnDiagramEntity> findBpmnDiagramEntitiesByBpmnRepositoryId(String bpmnDiagramRepositoryId);

    BpmnDiagramEntity findBpmnDiagramEntityByBpmnDiagramIdEquals(String bpmnDiagramId);

    BpmnDiagramEntity findBpmnDiagramEntityByBpmnRepositoryIdAndBpmnDiagramName(String bpmnRepositoryId, String bpmnDiagramName);

    int countAllByBpmnRepositoryId(String bpmnRepositoryId);

    int deleteBpmnDiagramEntityByBpmnDiagramId(String bpmnDiagramId);

    int deleteAllByBpmnRepositoryId(String bpmnRepositoryId);

    List<BpmnDiagramEntity> findBpmnDiagramEntitiesByBpmnRepositoryIdIn(List<String> bpmnRepositoryIds);
}
