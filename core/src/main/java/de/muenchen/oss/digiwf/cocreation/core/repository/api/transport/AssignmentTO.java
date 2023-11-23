package de.muenchen.oss.digiwf.cocreation.core.repository.api.transport;

import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Containing information about a user-repository relation")
public class AssignmentTO {

    @NotEmpty
    private String repositoryId;

    @NotEmpty
    private String userId;

    @NotNull
    private RoleEnum role;
}
