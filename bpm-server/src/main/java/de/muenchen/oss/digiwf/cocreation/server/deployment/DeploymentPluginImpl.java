package de.muenchen.oss.digiwf.cocreation.server.deployment;

import de.muenchen.oss.digiwf.cocreation.server.deployment.event.DeploymentEvent;
import de.muenchen.oss.digiwf.cocreation.server.deployment.handler.DeploymentHandler;
import de.muenchen.oss.digiwf.cocreation.core.artifact.plugin.DeploymentPlugin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeploymentPluginImpl implements DeploymentPlugin {

    private final Sinks.Many<Message<DeploymentEvent>> emitter;

    private final List<DeploymentHandler> deploymentHandlers;

    private final DeploymentTargetProperties deploymentTargetProperties;

    @Override
    public void deploy(final String deploymentId, final String versionId, final String target, final String file, final String artifactType) {
        // TODO what if target != getDeploymentTargets() ???

        final DeploymentEvent deploymentEvent = new DeploymentEvent(deploymentId, versionId, target, file, artifactType);

        final Message<DeploymentEvent> msg = this.deploymentHandlers.stream()
                .filter(handler -> handler.isResponsibleFor(artifactType))
                .findFirst()
                .orElseThrow()
                .createMessage(deploymentEvent);

        this.emitter.tryEmitNext(msg);
        log.info("Sent deployment event with deploymentId: {}, versionId: {}, target: {}, artifactType: {}", deploymentId, versionId, target, artifactType);
    }

    @Override
    public List<String> getDeploymentTargets() {
        return deploymentTargetProperties.getTargets();
    }
}
