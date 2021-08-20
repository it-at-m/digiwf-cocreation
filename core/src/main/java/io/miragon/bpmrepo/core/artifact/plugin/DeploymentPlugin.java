package io.miragon.bpmrepo.core.artifact.plugin;

import java.util.List;

public interface DeploymentPlugin {

    void deploy(String artifactType, String name, String artifact, String target);

    List<String> getDeploymentTargets();

}
