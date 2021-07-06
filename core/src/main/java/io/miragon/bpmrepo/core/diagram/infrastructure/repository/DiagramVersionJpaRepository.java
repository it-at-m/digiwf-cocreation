package io.miragon.bpmrepo.core.diagram.infrastructure.repository;

import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.DiagramVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface DiagramVersionJpaRepository extends JpaRepository<DiagramVersionEntity, String> {

    //findById is ONLY called if user requests one specific Diagram in order to edit it -> Lock is only applied in this case
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000000")})
    Optional<DiagramVersionEntity> findById(String versionId);

    List<DiagramVersionEntity> findAllByDiagramId(String bpmnDiagramId);

    Optional<DiagramVersionEntity> findFirstByDiagramIdOrderByReleaseDescMilestoneDesc(String bpmnDiagramId);

    DiagramVersionEntity findFirstByDiagramIdAndRepositoryIdOrderByReleaseDescMilestoneDesc(String bpmnDiagramId, String bpmnRepositoryId);

    int deleteAllByRepositoryId(String bpmnRepositoryId);

    int deleteAllByDiagramId(String bpmnDiagramId);

    int deleteAllByRepositoryIdAndDiagramIdAndSaveType(String bpmnRepositoryId, String bpmnDiagramId, SaveTypeEnum saveTypeEnum);

    @Lock(LockModeType.PESSIMISTIC_READ)
    DiagramVersionEntity save(DiagramVersionEntity entity);
}
