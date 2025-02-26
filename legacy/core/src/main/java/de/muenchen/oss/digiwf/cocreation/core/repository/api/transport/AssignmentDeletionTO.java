package de.muenchen.oss.digiwf.cocreation.core.repository.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Client created object for removing an user from a repository")
public class AssignmentDeletionTO {

    @NotEmpty
    private String repositoryId;

    @NotEmpty
    private String username;
}
