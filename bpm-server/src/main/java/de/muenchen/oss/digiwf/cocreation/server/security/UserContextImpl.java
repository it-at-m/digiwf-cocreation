package de.muenchen.oss.digiwf.cocreation.server.security;

import de.muenchen.oss.digiwf.cocreation.core.security.UserContext;
import de.muenchen.oss.digiwf.spring.security.SpringSecurityProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static io.muenchendigital.digiwf.spring.security.client.ClientParameters.fromEnvironment;

@Component
@Profile("!no-security")
public class UserContextImpl implements UserContext {

    private static final String NAME_UNAUTHENTICATED_USER = "unauthenticated";

    private final String userNameAttribute;

    public UserContextImpl(final SpringSecurityProperties springSecurityProperties, Environment environment) {
        this.userNameAttribute = fromEnvironment(environment, springSecurityProperties.getClientRegistration())
                .getUserNameAttribute();
    }

    @Override
    public String getUserName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken && authentication.getPrincipal() instanceof Jwt jwt) {
            // return username from token
            return Objects.requireNonNull((String) jwt.getClaims().get(userNameAttribute));
        } else {
            return NAME_UNAUTHENTICATED_USER;
        }
    }

}
