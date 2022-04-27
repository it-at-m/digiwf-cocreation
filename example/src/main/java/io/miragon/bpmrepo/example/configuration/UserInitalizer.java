package io.miragon.bpmrepo.example.configuration;

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
            this.userService.createUser("demo.user");
        } catch (final Exception error) {

        }
    }

}
