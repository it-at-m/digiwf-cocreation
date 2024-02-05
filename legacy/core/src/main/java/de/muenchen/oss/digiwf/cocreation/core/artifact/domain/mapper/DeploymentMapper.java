package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.mapper;


import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Deployment;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.DeploymentEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DeploymentMapper {
    Deployment toModel(DeploymentEntity entity);

    DeploymentEntity toEntity(Deployment model);

    List<Deployment> toModel(List<DeploymentEntity> entities);
}
