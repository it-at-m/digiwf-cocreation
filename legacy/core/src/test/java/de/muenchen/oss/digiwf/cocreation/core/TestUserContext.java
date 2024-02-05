package de.muenchen.oss.digiwf.cocreation.core;

import de.muenchen.oss.digiwf.cocreation.core.security.UserContext;
import org.springframework.stereotype.Component;

@Component
public class TestUserContext implements UserContext {

    @Override
    public String getUserName() {
        return "testmail";
    }
}
