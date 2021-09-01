package io.miragon.bpmrepo.core;

import io.miragon.bpmrepo.core.artifact.plugin.DeploymentPlugin;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultDeploymentPlugin implements DeploymentPlugin {

    @Override
    public void deploy(final String artifactType, final String name, final String artifact, final String target) {
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
