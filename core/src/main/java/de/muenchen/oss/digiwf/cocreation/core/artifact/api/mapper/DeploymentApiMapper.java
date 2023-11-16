package de.muenchen.oss.digiwf.cocreation.core.artifact.api.mapper;


import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.DeploymentTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.NewDeploymentTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Deployment;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.NewDeployment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DeploymentApiMapper {

    List<NewDeployment> mapToModel(final List<NewDeploymentTO> to);

    NewDeployment mapToModel(final NewDeploymentTO to);

    DeploymentTO mapToTO(final Deployment model);

    List<DeploymentTO> mapToTO(final List<Deployment> model);
}
