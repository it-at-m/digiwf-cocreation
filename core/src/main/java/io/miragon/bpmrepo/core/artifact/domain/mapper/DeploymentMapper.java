package io.miragon.bpmrepo.core.artifact.domain.mapper;


import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.DeploymentEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DeploymentMapper {
    Deployment toModel(DeploymentEntity entity);

    List<Deployment> toModel(List<DeploymentEntity> entities);
}
