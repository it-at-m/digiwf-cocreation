package de.muenchen.oss.digiwf.cocreation.core.artifact.adapter;

import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.enums.DeploymentStatus;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Deployment;
import org.springframework.stereotype.Component;

@Component
public class DeploymentAdapterImpl extends DeploymentAdapter {

    public Deployment successfulDeployment(final String deploymentId) {
        return this.updateDeployment(deploymentId, DeploymentStatus.SUCCESS, "Deployment was successful");
    }

    public Deployment failedDeployment(final String deploymentId) {
        return this.updateDeployment(deploymentId, DeploymentStatus.ERROR, "Deployment failed");
    }

}
