package de.muenchen.oss.digiwf.cocreation.core.repository.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
