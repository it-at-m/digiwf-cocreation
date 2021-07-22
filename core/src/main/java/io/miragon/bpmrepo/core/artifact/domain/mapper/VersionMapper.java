package io.miragon.bpmrepo.core.artifact.domain.mapper;

import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactVersionEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.DeploymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface VersionMapper {

    ArtifactVersionEntity mapToEntity(final ArtifactVersion model);

    @Mapping(target = "deployments", expression = "java(toModel(entity.getDeployments()))")
    ArtifactVersion mapToModel(final ArtifactVersionEntity entity);

    List<ArtifactVersion> mapToModel(List<ArtifactVersionEntity> list);

    List<Deployment> toModel(List<DeploymentEntity> a);
}
