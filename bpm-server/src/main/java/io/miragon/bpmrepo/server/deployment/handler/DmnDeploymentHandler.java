package io.miragon.bpmrepo.server.deployment.handler;


import io.miragon.bpmrepo.server.deployment.event.DeploymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Handle deployment for specific types
 */
@Profile("streaming")
@Component
@RequiredArgsConstructor
public class DmnDeploymentHandler implements DeploymentHandler {

    private final DeploymentProperties properties;

    @Override
    public boolean isResponsibleFor(final String artifactType) {
        return artifactType.equalsIgnoreCase("DMN");
    }

    @Override
    public Message<DeploymentEvent> createMessage(final DeploymentEvent deploymentEvent) {
        return MessageBuilder
                .withPayload(deploymentEvent)
                .setHeader("type", "deploy")
                .setHeader(STREAM_SEND_TO_DESTINATION, this.properties.getCocreation().get(deploymentEvent.getTarget().toUpperCase()))
                .build();
    }
}
