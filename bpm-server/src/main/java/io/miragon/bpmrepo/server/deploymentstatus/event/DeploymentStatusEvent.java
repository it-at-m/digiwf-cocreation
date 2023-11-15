package io.miragon.bpmrepo.server.deploymentstatus.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DeploymentStatusEvent {
    private String status;
    private String deploymentId;
    private String message;
}
