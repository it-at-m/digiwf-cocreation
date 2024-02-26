package de.muenchen.oss.digiwf.deployment;

import de.muenchen.oss.digiwf.spring.security.client.OAuth2AccessTokenSupplier;
import feign.RequestInterceptor;
import io.miragon.miranum.deploymentserver.adapter.out.MiranumRestDeployment;
import io.miragon.miranum.deploymentserver.application.ports.out.DeployFilePort;
import io.miragon.miranum.deploymentserver.properties.DeploymentServerRestProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DeploymentServerConfig {

    private final DeploymentServerRestProperties deploymentServerRestProperties;

    @Bean
    public DeployFilePort deployFilePort(RequestInterceptor requestInterceptor) {
        return new MiranumRestDeployment(this.deploymentServerRestProperties.getTargets(), List.of(requestInterceptor));
    }

    @Bean
    public RequestInterceptor oAuth2RequestInterceptor(OAuth2AccessTokenSupplier oAuth2AccessTokenSupplier) {
        return (requestTemplate ->
                requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + oAuth2AccessTokenSupplier.get().getTokenValue()));
    }

}
