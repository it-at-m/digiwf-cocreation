package io.miragon.bpmrepo.server.deploymentstatus.event;

import io.miragon.bpmrepo.core.artifact.adapter.DeploymentAdapter;
import io.miragon.bpmrepo.core.artifact.domain.enums.DeploymentStatus;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeploymentStatusEventConsumer extends DeploymentAdapter {

    @Bean
    public Consumer<Message<DeploymentStatusEvent>> deploymentStatus() {
        return message -> {
            try {
                log.info("Status update received {}", message.getPayload());

                final DeploymentStatusEvent event = message.getPayload();
                // map events status to DeploymentStatus
                final DeploymentStatus status = event.getStatus().equalsIgnoreCase("SUCCESSFUL") ? DeploymentStatus.SUCCESS : DeploymentStatus.ERROR;
                final var deployment = this.updateDeployment(event.getDeploymentId(), status, event.getMessage());
                log.info("Deployment {} status updated to {}", deployment.getId(), deployment.getStatus());
            } catch (final ObjectNotFoundException exception) {
                log.warn(exception.getMessage());
            }
        };
    }

}
