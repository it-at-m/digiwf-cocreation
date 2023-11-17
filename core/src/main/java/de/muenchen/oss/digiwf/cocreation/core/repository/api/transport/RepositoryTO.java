package de.muenchen.oss.digiwf.cocreation.core.repository.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Containing information about a repository")
public class RepositoryTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer existingArtifacts;

    @NotNull
    private Integer assignedUsers;

}
