package io.miragon.bpmrepo.core.repository.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;

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
