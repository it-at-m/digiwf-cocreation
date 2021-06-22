package io.miragon.bpmnrepo.core.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String userId;
    private String userName;

    public User(final String username) {
        this.userName = username;
    }

    public void updateUserName(final String username) {
        this.userName = username;
    }
}
