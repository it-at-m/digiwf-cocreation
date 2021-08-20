package io.miragon.bpmrepo.core.artifact.domain.service;

import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.plugin.DeploymentPlugin;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
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

    private final DeploymentPlugin deploymentPlugin;

    public ArtifactVersion deploy(final String artifactId, final String versionId, final String target, final User user) {
        log.debug("Persisting deployment of artifact version {} on target {} by user {}", versionId, target, user.getUsername());
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        final ArtifactVersion version = this.artifactVersionService.getVersion(versionId);
        this.deploymentPlugin.deploy(artifact.getFileType(), artifact.getName(), version.getXml(), target);
        version.deploy(target, user.getUsername());
        return this.artifactVersionService.saveToDb(version);
    }

    public List<String> getDeploymentTargets() {
        return this.deploymentPlugin.getDeploymentTargets();
    }

}
