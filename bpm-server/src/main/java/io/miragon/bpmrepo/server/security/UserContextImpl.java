package io.miragon.bpmrepo.server.security;

import io.miragon.bpmrepo.core.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Profile("!no-security")
@RequiredArgsConstructor
public class UserContextImpl implements UserContext {

    private static final String NAME_UNAUTHENTICATED_USER = "unauthenticated";
    private static final String TOKEN_USER_NAME = "user_name";

    @Override
    public String getUserName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            final OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
            final HashMap details = (HashMap) oauth2Authentication.getUserAuthentication().getDetails();
            return (String) details.get(TOKEN_USER_NAME);
        } else {
            return NAME_UNAUTHENTICATED_USER;
        }
    }

}
