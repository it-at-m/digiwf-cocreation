package io.miragon.bpmnrepo.spring.boot.starter;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "io.miragon.bpmnrepo.core")
@EntityScan(basePackages = "io.miragon.bpmnrepo.core")
@ComponentScan(basePackages = "io.miragon.bpmnrepo.core")
public class BpmnRepoAutoConfiguration {

}
