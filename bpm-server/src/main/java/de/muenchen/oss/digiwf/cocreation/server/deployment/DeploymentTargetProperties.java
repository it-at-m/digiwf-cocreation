package de.muenchen.oss.digiwf.cocreation.server.deployment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties("digiwf.deployment")
public class DeploymentTargetProperties {
    private List<String> targets;
}
