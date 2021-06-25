package io.miragon.bpmrepo.core.diagram.infrastructure.repository;

import io.miragon.bpmrepo.core.diagram.infrastructure.entity.StarredEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarredJpaRepository extends JpaRepository<StarredEntity, String> {

    int deleteById_DiagramIdAndId_UserId(String bpmnDiagramId, String userId);

    StarredEntity findById_DiagramIdAndId_UserId(String bpmnDiagramId, String userId);

    List<StarredEntity> findAllById_UserId(String userId);

}
