package io.miragon.bpmrepo.core.artifact.domain.plugin;

import java.util.List;

public interface DeploymentPlugin {

    void deploy(String name, String xml, String target);

    List<String> getDeploymentTargets();

}
