package de.muenchen.oss.digiwf.cocreation.core.repository.domain.model;

import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Assignment {
    private final String userId;
    private final String repositoryId;
    private final RoleEnum role;

}
