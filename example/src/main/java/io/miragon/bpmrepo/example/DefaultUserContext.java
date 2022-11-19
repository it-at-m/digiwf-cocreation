package io.miragon.bpmrepo.example;

import io.miragon.bpmrepo.core.security.UserContext;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserContext implements UserContext {

    @Override
    public String getUserName() {
        return "demo.user";
    }
}
