package io.miragon.bpmrepo.server.configuration;

import com.asyncapi.v2.binding.kafka.KafkaOperationBinding;
import com.asyncapi.v2.model.info.Info;
import com.asyncapi.v2.model.server.Server;
import io.miragon.bpmrepo.server.deployment.event.DeploymentEvent;
import io.miragon.bpmrepo.server.deployment.handler.DeploymentProperties;
import io.github.stavshamir.springwolf.asyncapi.types.ProducerData;
import io.github.stavshamir.springwolf.configuration.AsyncApiDocket;
import io.github.stavshamir.springwolf.configuration.EnableAsyncApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Profile("streaming")
@RequiredArgsConstructor
@Configuration
@EnableAsyncApi
public class CustomAsyncApiConfiguration {

    @Value("${spring.cloud.stream.default-binder}")
    private String binder;
    @Value("${spring.cloud.stream.kafka.binder.brokers}")
    private String broker;
    @Value("${io.muenchendigital.digiwf.docs.title}")
    private String title;
    @Value("${io.muenchendigital.digiwf.docs.version}")
    private String version;
    @Value("${io.muenchendigital.digiwf.docs.basePackage}")
    private String basePackage;
    private final DeploymentProperties properties;

    @Bean
    public AsyncApiDocket asyncApiDocket() {
        final Info info = Info.builder()
                .version(this.version)
                .title(this.title)
                .build();

        final Server server = Server.builder()
                .protocol(this.binder)
                .url(this.broker)
                .build();

        final List<ProducerData> producers = new ArrayList<>();
        this.properties.getCocreation().keySet().forEach(topic -> {
            producers.add(
                    ProducerData.builder()
                            .channelName(this.properties.getCocreation().get(topic))
                            .binding(Map.of(this.binder, new KafkaOperationBinding()))
                            .payloadType(DeploymentEvent.class)
                            .build()
            );
        });

        return AsyncApiDocket.builder()
                .basePackage(this.basePackage)
                .info(info)
                .server(this.binder, server)
                .producers(producers)
                .build();
    }
}
