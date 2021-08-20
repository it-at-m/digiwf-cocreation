package io.miragon.bpmrepo.core.artifact.domain.model;

import io.miragon.bpmrepo.core.artifact.api.transport.ShareWithRepositoryTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ShareWithTeamTO;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Shared {
    private String artifactId;
    private String repositoryId;
    private String teamId;
    private RoleEnum role;

    public Shared(final ShareWithRepositoryTO shareWithRepositoryTO) {
        this.artifactId = shareWithRepositoryTO.getArtifactId();
        this.repositoryId = shareWithRepositoryTO.getRepositoryId();
        this.teamId = "0";
        this.role = shareWithRepositoryTO.getRole();
    }

    public Shared(final ShareWithTeamTO shareWithTeamTO) {
        this.artifactId = shareWithTeamTO.getArtifactId();
        this.repositoryId = "0";
        this.teamId = shareWithTeamTO.getTeamId();
        this.role = shareWithTeamTO.getRole();
    }

    public void updateRole(final RoleEnum role) {
        this.role = role;
    }
}
