package io.miragon.bpmrepo.core.user;

import io.miragon.bpmrepo.core.user.domain.model.User;

public class UserBuilder {

    public static User buildUser(final String id, final String username) {
        return User.builder()
                .id(id)
                .username(username)
                .build();
    }
}
