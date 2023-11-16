package de.muenchen.oss.digiwf.cocreation.example;

import de.muenchen.oss.digiwf.cocreation.core.security.UserContext;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserContext implements UserContext {

    @Override
    public String getUserName() {
        return "demo.user";
    }
}
