package io.miragon.bpmrepo.spring.boot.starter;

import io.miragon.bpmrepo.core.artifact.plugin.ArtifactTypesPlugin;
import io.miragon.bpmrepo.core.artifact.plugin.DeploymentPlugin;
import io.miragon.bpmrepo.core.menu.api.plugin.MenuPlugin;
import io.miragon.bpmrepo.spring.boot.starter.deployment.DefaultDeploymentPlugin;
import io.miragon.bpmrepo.spring.boot.starter.fileTypes.DefaultFileTypesPlugin;
import io.miragon.bpmrepo.spring.boot.starter.menu.DefaultMenuPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "io.miragon.bpmrepo.core")
@EntityScan(basePackages = "io.miragon.bpmrepo.core")
@ComponentScan(basePackages = "io.miragon.bpmrepo.core")
public class BpmRepoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DeploymentPlugin deploymentPlugin() {
        return new DefaultDeploymentPlugin();
    }

    @Bean
    @ConditionalOnMissingBean
    public MenuPlugin menuPlugin() {
        return new DefaultMenuPlugin();
    }

    @Bean
    @ConditionalOnMissingBean
    public ArtifactTypesPlugin fileTypesPlugin() {
        return new DefaultFileTypesPlugin();
    }

}
