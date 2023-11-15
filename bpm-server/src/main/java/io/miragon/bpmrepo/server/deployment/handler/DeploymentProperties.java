package io.miragon.bpmrepo.server.deployment.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "digiwf.streaming.topics")
public class DeploymentProperties {

    private Map<String, String> cocreation;

}
