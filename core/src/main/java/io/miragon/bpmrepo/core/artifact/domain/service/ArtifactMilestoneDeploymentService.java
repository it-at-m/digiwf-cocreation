package io.miragon.bpmrepo.core.artifact.domain.service;

import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.artifact.domain.model.NewDeployment;
import io.miragon.bpmrepo.core.artifact.plugin.DeploymentPlugin;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import io.miragon.bpmrepo.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtifactMilestoneDeploymentService {

    private final ArtifactMilestoneService artifactMilestoneService;
    private final ArtifactService artifactService;
    private final AuthService authService;
    private final LockService lockService;


    private final DeploymentPlugin deploymentPlugin;

    public ArtifactMilestone deploy(final String artifactId, final String versionId, final String target, final User user) {
        log.debug("Persisting deployment of artifact version {} on target {} by user {}", versionId, target, user.getUsername());
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(artifact);
        final ArtifactMilestone version = this.artifactMilestoneService.getMilestone(versionId).orElseThrow(() -> new ObjectNotFoundException("exception.versionNotFound"));
        //Check if the version is already deployed to the specified target - If true, overwrite the Deployment Object - If false, create a new Deployment Object
        final ArtifactMilestone deployedVersion = this.createOrUpdateDeployment(version, target, user.getUsername());
        this.deploymentPlugin.deploy(deployedVersion.getId(), target);
        return this.artifactMilestoneService.saveToDb(version);
    }

    public List<ArtifactMilestone> deployMultiple(final List<NewDeployment> deployments, final User user) {
        log.debug("Persisting deployments of {} versions to target {} by user {}", deployments.size(), deployments.get(0).getTarget(), user.getUsername());
        final List<String> artifactIds = deployments.stream().map(NewDeployment::getArtifactId).collect(Collectors.toList());
        final List<Artifact> artifacts = this.artifactService.getAllArtifactsById(artifactIds);
        artifacts.forEach(artifact -> {
            this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
            this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(artifact);
        });


        //Stream through all versions that have to be deployed
        final List<ArtifactMilestone> updatedVersions = deployments.stream().map(deployment -> {
            final ArtifactMilestone version = this.artifactMilestoneService.getLatestMilestone(deployment.getArtifactId());
            //Check if the version is already deployed to the specified target - If true, overwrite the Deployment Object - If false, create a new Deployment Object
            final ArtifactMilestone deployedVersion = this.createOrUpdateDeployment(version, deployment.getTarget(), user.getUsername());
            this.deploymentPlugin.deploy(deployedVersion.getId(), deployment.getTarget());
            return this.artifactMilestoneService.saveToDb(deployedVersion);
        }).collect(Collectors.toList());

        return updatedVersions;
    }

    public ArtifactMilestone createOrUpdateDeployment(final ArtifactMilestone version, final String target, final String username) {
        final Optional<Deployment> existingDeployment = this.getExistingDeployment(version, target);
        if (existingDeployment.isPresent()) {
            version.updateDeployment(existingDeployment.get(), username);
        } else {
            version.deploy(target, username);
        }
        return version;
    }

    public List<String> getDeploymentTargets() {
        return this.deploymentPlugin.getDeploymentTargets();
    }


    public Optional<Deployment> getExistingDeployment(final ArtifactMilestone version, final String target) {
        return version.getDeployments().stream()
                .filter(existingDeployments -> existingDeployments.getTarget().equals(target))
                .findFirst();
    }

}
