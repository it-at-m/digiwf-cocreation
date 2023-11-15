package io.miragon.bpmrepo.server.deployment.handler;

import io.miragon.bpmrepo.server.deployment.event.DeploymentEvent;
import org.springframework.messaging.Message;

/**
 * Handle deployment for specific types
 */
public interface DeploymentHandler {

    boolean isResponsibleFor(String artifactType);

    Message<DeploymentEvent> createMessage(final DeploymentEvent deploymentEvent);

    String STREAM_SEND_TO_DESTINATION = "spring.cloud.stream.sendto.destination";

}
