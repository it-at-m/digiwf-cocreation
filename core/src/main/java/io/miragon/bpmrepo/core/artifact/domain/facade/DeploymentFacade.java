package io.miragon.bpmrepo.core.artifact.domain.facade;

import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.artifact.domain.model.NewDeployment;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactMilestoneService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.DeploymentService;
import io.miragon.bpmrepo.core.artifact.domain.service.LockService;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import io.miragon.bpmrepo.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeploymentFacade {

    private final ArtifactMilestoneService artifactMilestoneService;
    private final ArtifactService artifactService;
    private final AuthService authService;
    private final LockService lockService;

    private final DeploymentService deploymentService;

    public ArtifactMilestone deploy(final NewDeployment newDeployment, final User user) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(newDeployment.getArtifactId());
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        final ArtifactMilestone milestone = this.artifactMilestoneService.getMilestone(newDeployment.getMilestoneId())
                .orElseThrow(() -> new ObjectNotFoundException("exception.versionNotFound"));
        return this.deploymentService.deploy(milestone, newDeployment, user.getUsername());
    }

    public List<ArtifactMilestone> deployMultiple(final List<NewDeployment> deployments, final User user) {
        log.debug("Checking permissions");
        final List<String> artifactIds = deployments.stream().map(NewDeployment::getArtifactId).collect(Collectors.toList());
        final List<Artifact> artifacts = this.artifactService.getAllArtifactsById(artifactIds);
        artifacts.forEach(artifact -> {
            this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        });

        return this.deploymentService.deployMultiple(deployments, user.getUsername());

    }

    public List<Deployment> getAllDeploymentsFromRepository(final String repositoryId) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.deploymentService.getAllDeploymentsFromRepository(repositoryId);

    }

    public List<String> getDeploymentTargets() {
        return this.deploymentService.getDeploymentTargets();
    }

}
