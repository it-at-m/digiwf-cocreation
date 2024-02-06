package de.muenchen.oss.digiwf.deployment;

import feign.RequestInterceptor;
import io.miragon.miranum.deploymentserver.adapter.out.MiranumRestDeployment;
import io.miragon.miranum.deploymentserver.application.ports.out.DeployFilePort;
import io.miragon.miranum.deploymentserver.properties.DeploymentServerRestProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DeploymentServerConfig {

    private final RequestInterceptor requestInterceptor;
    private final DeploymentServerRestProperties deploymentServerRestProperties;

    @Bean
    public DeployFilePort deployFilePort() {
        return new MiranumRestDeployment(this.deploymentServerRestProperties.getTargets(), List.of(requestInterceptor));
    }

}
