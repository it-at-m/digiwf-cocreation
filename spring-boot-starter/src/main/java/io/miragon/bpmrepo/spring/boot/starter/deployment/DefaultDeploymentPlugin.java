package io.miragon.bpmrepo.spring.boot.starter.deployment;

import io.miragon.bpmrepo.core.artifact.plugin.DeploymentPlugin;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DefaultDeploymentPlugin implements DeploymentPlugin {

    @Override
    public void deploy(final String artifactType, final String name, final String artifact, final String target) {
        log.error("Deployment executed, but no Plugin available Name: {} target: {}", name, target);
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
