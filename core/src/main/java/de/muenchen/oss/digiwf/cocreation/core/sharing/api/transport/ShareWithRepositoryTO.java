package de.muenchen.oss.digiwf.cocreation.core.sharing.api.transport;

import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareWithRepositoryTO {

    @NotBlank
    private String artifactId;

    @NotBlank
    private String repositoryId;

    @NotNull
    private RoleEnum role;

}
