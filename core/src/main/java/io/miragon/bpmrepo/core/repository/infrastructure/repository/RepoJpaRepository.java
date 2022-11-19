package io.miragon.bpmrepo.core.repository.infrastructure.repository;

import io.miragon.bpmrepo.core.repository.infrastructure.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoJpaRepository extends JpaRepository<RepositoryEntity, String> {
    List<RepositoryEntity> findAllByNameIsNot(String name);

    RepositoryEntity findByIdAndName(String bpmnRepositoryId, String bpmnRepositoryName);
}
