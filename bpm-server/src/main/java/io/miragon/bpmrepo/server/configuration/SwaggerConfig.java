package io.miragon.bpmrepo.server.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

/**
 *
 */
@Configuration
public class SwaggerConfig {

    private final String authServer;
    private final String realm;

    @Autowired
    public SwaggerConfig(@Value("${keycloak.auth-server-url}") final String authServer,
            @Value("${realm}") final String realm) {
        this.authServer = authServer;
        this.realm = realm;
    }

    @Bean
    public OpenAPI openAPI() {
        final var authUrl = String.format("%s/realms/%s/protocol/openid-connect", this.authServer, this.realm);

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("spring_oauth", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("Oauth2 flow")
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .flows(new OAuthFlows()
                                        .password(new OAuthFlow()
                                                .authorizationUrl(authUrl + "/auth")
                                                .refreshUrl(authUrl + "/token")
                                                .tokenUrl(authUrl + "/token")
                                                .scopes(new Scopes()
                                                        .addString("lhm_extended", "lhm_extended")))))
                )
                .security(Arrays.asList(
                        new SecurityRequirement().addList("spring_oauth")))
                .info(new Info()
                        .title("DigiWF BPM Server API")
                        .description("DigiWF BPM Server- Plattform zur Entwicklung von Workflows bei der LHM")
                        .contact(new Contact()
                                .name("DigiWF")
                                .email("itm.digiwf@muenchen.de")))
                .externalDocs(new ExternalDocumentation()
                        .description("Externe Dokumentation auf unserer Wilma-Seite")
                        .url("https://wilma.muenchen.de/workspaces/digitale-workflows/apps/wiki/wiki/list"));
    }

    @Bean
    @Profile("!prod")
    public String[] whitelist() {
        return new String[] {
                // -- swagger ui
                "/v2/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
        };
    }

    @Bean
    @Profile("prod")
    public String[] whitelistProd() {
        return new String[] {};
    }

}
