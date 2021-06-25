package io.miragon.bpmrepo.core;

import io.miragon.bpmrepo.core.security.UserContext;
import org.springframework.stereotype.Component;

@Component
public class TestUserContext implements UserContext {

    @Override
    public String getUserName() {
        return "testmail";
    }
}
