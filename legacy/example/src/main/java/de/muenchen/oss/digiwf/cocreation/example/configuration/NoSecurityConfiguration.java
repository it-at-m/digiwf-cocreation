package de.muenchen.oss.digiwf.cocreation.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@Profile(NoSecurityConfiguration.NO_SECURITY)
public class NoSecurityConfiguration {

    /**
     * De-activates security.
     */
    public static final String NO_SECURITY = "no-security";

    @Bean
    public SecurityFilterChain mainSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // @formatter:off
        httpSecurity
                .headers()
                .frameOptions().disable()
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .csrf()
                .disable();
        // @formatter:on
        return httpSecurity.build();
    }
}
