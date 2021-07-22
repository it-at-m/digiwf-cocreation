package io.miragon.bpmrepo.spring.boot.starter;

import io.miragon.bpmrepo.core.artifact.api.plugin.FileTypesPlugin;
import io.miragon.bpmrepo.core.artifact.domain.plugin.DeploymentPlugin;
import io.miragon.bpmrepo.core.menu.api.plugin.MenuPlugin;
import io.miragon.bpmrepo.spring.boot.starter.artifact.DefaultDeploymentPlugin;
import io.miragon.bpmrepo.spring.boot.starter.artifact.DefaultFileTypesPlugin;
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
    public FileTypesPlugin fileTypesPlugin() {
        return new DefaultFileTypesPlugin();
    }

}
