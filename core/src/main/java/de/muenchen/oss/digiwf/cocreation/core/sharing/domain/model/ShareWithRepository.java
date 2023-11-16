package de.muenchen.oss.digiwf.cocreation.core.sharing.domain.model;


import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShareWithRepository {

    private String artifactId;
    private String repositoryId;
    private RoleEnum role;
}
