package de.muenchen.oss.digiwf.cocreation.spring.boot.starter.deployment;

import de.muenchen.oss.digiwf.cocreation.core.artifact.plugin.DeploymentPlugin;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DefaultDeploymentPlugin implements DeploymentPlugin {

    @Override
    public void deploy(final String deploymentId, final String versionId, final String target, final String file, final String artifactType) {
        log.info("Deployed version {} to target {}", versionId, target);
    }

    @Override
    public List<String> getDeploymentTargets() {
        final List<String> list = new ArrayList<>();
        list.add("Produktion");
        list.add("Review");
        list.add("Management");

        return list;
    }
}
