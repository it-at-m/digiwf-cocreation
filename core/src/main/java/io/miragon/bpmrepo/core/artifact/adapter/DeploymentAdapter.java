package io.miragon.bpmrepo.core.artifact.adapter;

import io.miragon.bpmrepo.core.artifact.domain.enums.DeploymentStatus;
import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.artifact.domain.service.DeploymentService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DeploymentAdapter {

    @Autowired
    private DeploymentService deploymentService;

    /**
     * The status of a deployment can be *PENDING*, *SUCCESS* or *ERROR*. Every deployment has the status *PENDING* until you change it.
     * <p>
     * To update the status of a deployment you can implement a `DeploymentAdapterImpl` that calls the `updateDeployment(...)` Method
     * on the `DeploymentAdapter` base calls.
     * </p>
     *
     * @param deploymentId
     * @param status
     * @param message
     * @return Deployment
     */
    public Deployment updateDeployment(final String deploymentId, final DeploymentStatus status, final String message) {
        return this.deploymentService.updateDeploymentStatus(deploymentId, status, message);
    }

}
