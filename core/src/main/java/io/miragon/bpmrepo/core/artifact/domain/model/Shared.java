package io.miragon.bpmrepo.core.artifact.domain.model;

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

    public Shared(final ShareWithRepository shareWithRepository) {
        this.artifactId = shareWithRepository.getArtifactId();
        this.repositoryId = shareWithRepository.getRepositoryId();
        this.teamId = "0";
        this.role = shareWithRepository.getRole();
    }

    public Shared(final ShareWithTeam shareWithTeam) {
        this.artifactId = shareWithTeam.getArtifactId();
        this.repositoryId = "0";
        this.teamId = shareWithTeam.getTeamId();
        this.role = shareWithTeam.getRole();
    }

    public void updateRole(final RoleEnum role) {
        this.role = role;
    }
}
