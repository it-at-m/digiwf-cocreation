package io.miragon.bpmrepo.server.deployment.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class DeploymentEmitterConfig {

    @Bean
    public Sinks.Many<Message<DeploymentEvent>> deploymentSink() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<Message<DeploymentEvent>>> deployArtifact(final Sinks.Many<Message<DeploymentEvent>> sink) {
        return sink::asFlux;
    }
}
