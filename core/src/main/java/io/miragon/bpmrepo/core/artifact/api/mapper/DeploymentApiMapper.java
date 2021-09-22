package io.miragon.bpmrepo.core.artifact.api.mapper;


import io.miragon.bpmrepo.core.artifact.api.transport.NewDeploymentTO;
import io.miragon.bpmrepo.core.artifact.domain.model.NewDeployment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DeploymentApiMapper {
    List<NewDeployment> mapToModel(final List<NewDeploymentTO> to);
}
