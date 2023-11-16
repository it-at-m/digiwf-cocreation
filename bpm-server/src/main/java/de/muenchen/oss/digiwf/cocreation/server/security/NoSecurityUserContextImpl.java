package de.muenchen.oss.digiwf.cocreation.server.security;

import de.muenchen.oss.digiwf.cocreation.core.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("no-security")
@RequiredArgsConstructor
public class NoSecurityUserContextImpl implements UserContext {

    @Override
    public String getUserName() {
       return "john.doe";
    }


}
