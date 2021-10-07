package io.miragon.bpmrepo.core.artifact.domain.service;

import io.miragon.bpmrepo.core.artifact.domain.mapper.DeploymentMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.artifact.domain.model.NewDeployment;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.DeploymentJpaRepository;
import io.miragon.bpmrepo.core.artifact.plugin.DeploymentPlugin;
import io.miragon.bpmrepo.core.shared.exception.AlreadyDeployedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeploymentService {

    private final ArtifactMilestoneService artifactMilestoneService;
    private final DeploymentJpaRepository deploymentJpaRepository;
    private final DeploymentMapper mapper;
    private final DeploymentPlugin deploymentPlugin;

    public ArtifactMilestone deploy(final ArtifactMilestone milestone, final NewDeployment newDeployment, final String username) {
        //Check if the version is already deployed to the specified target - If true, return an exception - If false, create a new Deployment Object
        final ArtifactMilestone deployedVersion = this.createOrUpdateDeployment(milestone, newDeployment, username);
        this.deploymentPlugin.deploy(deployedVersion.getId(), newDeployment.getTarget());
        return this.artifactMilestoneService.saveToDb(milestone);
    }

    public List<ArtifactMilestone> deployMultiple(final List<NewDeployment> deployments, final String username) {

        //Stream through all versions that have to be deployed
        final List<ArtifactMilestone> updatedMilestones = deployments.stream().map(deployment -> {
            final ArtifactMilestone version = this.artifactMilestoneService.getLatestMilestone(deployment.getArtifactId());
            //Check if the version is already deployed to the specified target - If true, overwrite the Deployment Object - If false, create a new Deployment Object
            final ArtifactMilestone deployedVersion = this.createOrUpdateDeployment(version, deployment, username);
            this.deploymentPlugin.deploy(deployedVersion.getId(), deployment.getTarget());
            return this.artifactMilestoneService.saveToDb(deployedVersion);
        }).collect(Collectors.toList());

        return updatedMilestones;
    }

    private ArtifactMilestone createOrUpdateDeployment(final ArtifactMilestone version, final NewDeployment newDeployment, final String username) {
        final Optional<Deployment> existingDeployment = this.getExistingDeployment(version, newDeployment.getTarget());
        if (existingDeployment.isPresent()) {
            throw new AlreadyDeployedException("exception.alreadyDeployed");
            //version.updateDeployment(existingDeployment.get(), username);
        } else {
            version.deploy(newDeployment, username);
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

    public List<Deployment> getAllDeploymentsFromRepository(final String repositoryId) {
        return this.mapper.toModel(this.deploymentJpaRepository.findAllByRepositoryId(repositoryId));
    }
}
