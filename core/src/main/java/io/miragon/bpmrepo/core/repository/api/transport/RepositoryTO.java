package io.miragon.bpmrepo.core.repository.api.transport;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    @NotNull
    private List<ArtifactTO> sharedArtifacts = new ArrayList<>();

}
