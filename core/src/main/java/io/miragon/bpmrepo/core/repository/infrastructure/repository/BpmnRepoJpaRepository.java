package io.miragon.bpmrepo.core.repository.infrastructure.repository;

import io.miragon.bpmrepo.core.repository.infrastructure.entity.BpmnRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//@Repository
public interface BpmnRepoJpaRepository extends JpaRepository<BpmnRepositoryEntity, String> {
    List<BpmnRepositoryEntity> findAllByBpmnRepositoryNameIsNot(String bpmnRepositoryName);

    List<BpmnRepositoryEntity> findAllByBpmnRepositoryId(String bpmnRepositoryId);

    BpmnRepositoryEntity findByBpmnRepositoryIdAndBpmnRepositoryName(String bpmnRepositoryId, String bpmnRepositoryName);

    void deleteBpmnRepositoryEntityByBpmnRepositoryId(String bpmnRepositoryId);

    BpmnRepositoryEntity findByBpmnRepositoryId(String bpmnRepositoryId);

    Optional<BpmnRepositoryEntity> findByBpmnRepositoryIdEquals(String bpmnRepositoryId);
}
