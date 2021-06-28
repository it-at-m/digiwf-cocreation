package io.miragon.bpmrepo.core.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class User {

    private String id;
    private String username;

    public User(final String username) {
        this.username = username;
    }

    public void updateUserName(final String username) {
        this.username = username;
    }
}
