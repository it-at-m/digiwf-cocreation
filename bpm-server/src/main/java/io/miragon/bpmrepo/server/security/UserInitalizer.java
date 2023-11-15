package io.miragon.bpmrepo.server.security;

import io.miragon.bpmrepo.core.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class UserInitalizer {

    private final UserService userService;

    @PostConstruct
    public void init() {
        try {
            this.userService.createUser("john.doe");
        } catch (final Exception error) {

        }
    }

}
