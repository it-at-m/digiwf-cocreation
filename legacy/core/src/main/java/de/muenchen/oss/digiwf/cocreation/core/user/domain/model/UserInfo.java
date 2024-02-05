package de.muenchen.oss.digiwf.cocreation.core.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class UserInfo {

    private final String username;

    private final String id;
}
