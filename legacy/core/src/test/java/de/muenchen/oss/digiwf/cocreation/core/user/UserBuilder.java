package de.muenchen.oss.digiwf.cocreation.core.user;

import de.muenchen.oss.digiwf.cocreation.core.user.domain.model.User;

public class UserBuilder {

    public static User buildUser(final String id, final String username) {
        return User.builder()
                .id(id)
                .username(username)
                .build();
    }
}
