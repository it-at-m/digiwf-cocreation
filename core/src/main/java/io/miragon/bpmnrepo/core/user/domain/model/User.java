package io.miragon.bpmnrepo.core.user.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String userId;
    private String userName;

    public User(final String username) {
        this.userName = username;

    }
}
