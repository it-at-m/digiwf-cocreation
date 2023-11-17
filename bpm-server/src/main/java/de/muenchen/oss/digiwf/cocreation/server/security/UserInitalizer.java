package de.muenchen.oss.digiwf.cocreation.server.security;

import de.muenchen.oss.digiwf.cocreation.core.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

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
