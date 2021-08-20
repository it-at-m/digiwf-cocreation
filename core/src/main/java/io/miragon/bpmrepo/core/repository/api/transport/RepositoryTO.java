package io.miragon.bpmrepo.core.repository.api.transport;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTO;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotNull
    private String description;

    @Nullable
    private List<ArtifactTO> sharedArtifacts = new ArrayList<>();

    @NotNull
    private Integer existingArtifacts;

    @NotNull
    private Integer assignedUsers;
}
