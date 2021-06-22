package io.miragon.bpmnrepo.core.diagram.infrastructure.repository;

import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BpmnDiagramVersionJpaRepository extends JpaRepository<BpmnDiagramVersionEntity, String> {

    List<BpmnDiagramVersionEntity> findAllByBpmnDiagramId(String bpmnDiagramId);

    BpmnDiagramVersionEntity findAllByBpmnDiagramVersionIdEquals(String bpmnDiagramVersionId);

    BpmnDiagramVersionEntity findFirstByBpmnDiagramIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(String bpmnDiagramId);

    BpmnDiagramVersionEntity findFirstByBpmnDiagramIdAndBpmnRepositoryIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(
            String bpmnDiagramId, String bpmnRepositoryId);

    int deleteAllByBpmnRepositoryId(String bpmnRepositoryId);

    int deleteAllByBpmnDiagramId(String bpmnDiagramId);

    int deleteAllByBpmnRepositoryIdAndBpmnDiagramIdAndSaveType(String bpmnRepositoryId, String bpmnDiagramId, SaveTypeEnum saveTypeEnum);
}
