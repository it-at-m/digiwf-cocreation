package io.miragon.bpmrepo.core.artifact.plugin;

import java.util.List;

public interface DeploymentPlugin {

    //TODO: why is the artifactType specified here? -> only VersionId and target are essential, would make things easier to only use them
    void deploy(String versionId, String target);

    List<String> getDeploymentTargets();

}
