package io.miragon.bpmrepo.spring.boot.starter.artifact;

import io.miragon.bpmrepo.core.artifact.domain.plugin.DeploymentPlugin;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DefaultDeploymentPlugin implements DeploymentPlugin {

    @Override
    public void deploy(final String name, final String xml, final String target) {
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
