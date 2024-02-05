package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.mapper;

import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactMilestone;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Deployment;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.ArtifactMilestoneEntity;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.DeploymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface MilestoneMapper {

    ArtifactMilestoneEntity mapToEntity(final ArtifactMilestone model);

    @Mapping(target = "deployments", expression = "java(toModel(entity.getDeployments()))")
    ArtifactMilestone mapToModel(final ArtifactMilestoneEntity entity);

    List<ArtifactMilestone> mapToModel(List<ArtifactMilestoneEntity> list);

    List<Deployment> toModel(List<DeploymentEntity> a);
}
