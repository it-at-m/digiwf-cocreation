package de.muenchen.oss.digiwf.cocreation.spring.boot.starter;

import de.muenchen.oss.digiwf.cocreation.core.artifact.plugin.ArtifactTypesPlugin;
import de.muenchen.oss.digiwf.cocreation.core.artifact.plugin.DeploymentPlugin;
import de.muenchen.oss.digiwf.cocreation.spring.boot.starter.deployment.DefaultDeploymentPlugin;
import de.muenchen.oss.digiwf.cocreation.spring.boot.starter.fileTypes.DefaultFileTypesPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "de.muenchen.oss.digiwf.cocreation.core")
@EntityScan(basePackages = "de.muenchen.oss.digiwf.cocreation.core")
@ComponentScan(basePackages = "de.muenchen.oss.digiwf.cocreation.core")
public class BpmRepoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DeploymentPlugin deploymentPlugin() {
        return new DefaultDeploymentPlugin();
    }

    @Bean
    @ConditionalOnMissingBean
    public ArtifactTypesPlugin fileTypesPlugin() {
        return new DefaultFileTypesPlugin();
    }

}
