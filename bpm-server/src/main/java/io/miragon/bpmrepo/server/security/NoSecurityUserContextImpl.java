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
@Profile("no-security")
@RequiredArgsConstructor
public class NoSecurityUserContextImpl implements UserContext {

    @Override
    public String getUserName() {
       return "john.doe";
    }


}
