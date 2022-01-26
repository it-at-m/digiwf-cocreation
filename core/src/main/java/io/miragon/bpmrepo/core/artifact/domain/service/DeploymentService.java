package io.miragon.bpmrepo.core.artifact.domain.service;

import io.miragon.bpmrepo.core.artifact.domain.mapper.DeploymentMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.artifact.domain.model.NewDeployment;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.DeploymentJpaRepository;
import io.miragon.bpmrepo.core.artifact.plugin.DeploymentPlugin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeploymentService {

    private final ArtifactMilestoneService artifactMilestoneService;
    private final DeploymentJpaRepository deploymentJpaRepository;
    private final DeploymentMapper mapper;
    private final DeploymentPlugin deploymentPlugin;

    public ArtifactMilestone deploy(final ArtifactMilestone milestone, final NewDeployment newDeployment, final Artifact artifact, final String username) {
        //Check if the version is already deployed to the specified target - If true, return an exception - If false, create a new Deployment Object
        final String deploymentId = newDeployment.generateId();
        final ArtifactMilestone deployedVersion = this.createDeployment(milestone, newDeployment, username);
        this.deploymentPlugin.deploy(deploymentId, deployedVersion.getId(), newDeployment.getTarget(), deployedVersion.getFile(), artifact.getFileType());
        return this.artifactMilestoneService.saveToDb(milestone);
    }

    private ArtifactMilestone createDeployment(final ArtifactMilestone version, final NewDeployment newDeployment, final String username) {
        version.deploy(newDeployment, username);
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
