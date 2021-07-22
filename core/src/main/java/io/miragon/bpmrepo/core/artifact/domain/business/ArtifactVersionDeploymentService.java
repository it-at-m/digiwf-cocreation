package io.miragon.bpmrepo.core.artifact.domain.business;

import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.domain.plugin.DeploymentPlugin;
import io.miragon.bpmrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
import io.miragon.bpmrepo.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtifactVersionDeploymentService {

    private final ArtifactVersionService artifactVersionService;
    private final ArtifactService artifactService;
    private final AuthService authService;
    private final UserService userService;

    private final DeploymentPlugin deploymentPlugin;

    public void deploy(final String artifactId, final String versionId, final String target) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        final User user = this.userService.getCurrentUser();
        log.debug("Deploy artifact version {} on target {} by user {}", versionId, target, user);
        final ArtifactVersion version = this.artifactVersionService.getVersion(versionId);
        this.deploymentPlugin.deploy(artifact.getName(), version.getXml(), target);
        version.deploy(target, user.getUsername());
        this.artifactVersionService.saveToDb(version);
    }

    public List<String> getDeploymentTargets() {
        return this.deploymentPlugin.getDeploymentTargets();
    }

}
