package io.miragon.bpmrepo.core.artifact.infrastructure.repository;

import io.miragon.bpmrepo.core.artifact.infrastructure.entity.DeploymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeploymentJpaRepository extends JpaRepository<DeploymentEntity, String> {

    List<DeploymentEntity> findAllByRepositoryId(String repositoryId);

}


