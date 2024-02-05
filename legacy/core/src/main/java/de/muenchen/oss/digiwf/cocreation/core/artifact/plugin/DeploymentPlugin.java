package de.muenchen.oss.digiwf.cocreation.core.artifact.plugin;

import java.util.List;

public interface DeploymentPlugin {

    void deploy(String deploymentId, String versionId, String target, String file, String artifactType);

    List<String> getDeploymentTargets();

}
