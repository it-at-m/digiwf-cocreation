package io.miragon.bpmnrepo.core;

import io.miragon.bpmnrepo.core.security.UserContext;
import org.springframework.stereotype.Component;

@Component
public class TestUserContext implements UserContext {

    @Override
    public String getUserName() {
        return "testmail";
    }
}
