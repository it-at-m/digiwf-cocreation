package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.service;

import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.repository.DeploymentJpaRepository;
import de.muenchen.oss.digiwf.cocreation.core.artifact.plugin.ArtifactTypesPlugin;
import de.muenchen.oss.digiwf.cocreation.core.artifact.plugin.DeploymentPlugin;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactTypeTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.enums.DeploymentStatus;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.mapper.DeploymentMapper;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Artifact;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactMilestone;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Deployment;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.NewDeployment;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.AlreadyDeployedException;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.ObjectNotFoundException;
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
    private final ArtifactTypesPlugin artifactTypesPlugin;

    public ArtifactMilestone deploy(final ArtifactMilestone milestone, final NewDeployment newDeployment, final Artifact artifact, final String username) {
        // if artifact type is not deployable raise an exception
        final Optional<ArtifactTypeTO> artifactType = this.artifactTypesPlugin.getArtifactTypes()
                .stream().filter(type -> type.getName().equalsIgnoreCase(artifact.getFileType()) && type.isDeployable()).findAny();
        if (artifactType.isEmpty()) {
            final String errorMsg = String.format("Artifacts of type %s are not deployable", artifact.getFileType());
            log.warn(errorMsg);
            throw new AlreadyDeployedException(errorMsg);
        }

        //Check if the version is already deployed to the specified target - If true, return an exception - If false, create a new Deployment Object
        ArtifactMilestone deployedVersion = this.createDeployment(milestone, newDeployment, username);
        deployedVersion = this.artifactMilestoneService.saveToDb(deployedVersion);
        // NOTE: the last element of deployedVersion.getDeployments() is the new deployment
        final String deploymentId = deployedVersion.getDeployments().get(deployedVersion.getDeployments().size() - 1).getId();
        this.deploymentPlugin.deploy(deploymentId, deployedVersion.getId(), newDeployment.getTarget(), deployedVersion.getFile(), artifact.getFileType());
        return deployedVersion;
    }

    private ArtifactMilestone createDeployment(final ArtifactMilestone version, final NewDeployment newDeployment, final String username) {
        version.deploy(newDeployment, username);
        return version;
    }

    /**
     * Update a deployments status
     *
     * @param deploymentId
     * @param status
     * @param message
     * @return Deployment
     */
    public Deployment updateDeploymentStatus(final String deploymentId, final DeploymentStatus status, final String message) {
        final Deployment deployment = this.deploymentJpaRepository.findById(deploymentId)
                .map(this.mapper::toModel)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Deployment with id %s not found!", deploymentId)));
        deployment.update(status, message);
        return this.mapper.toModel(this.deploymentJpaRepository.save(this.mapper.toEntity(deployment)));
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
