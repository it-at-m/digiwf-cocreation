package io.miragon.bpmrepo.core.artifact.domain.mapper;


import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.DeploymentEntity;
import org.mapstruct.Mapper;

@Mapper
public interface DeploymentMapper {
    Deployment toModel(DeploymentEntity deploymentEntity);
}
