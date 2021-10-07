package io.miragon.bpmrepo.core.artifact.api.resource;

import io.miragon.bpmrepo.core.artifact.api.mapper.ArtifactMilestoneApiMapper;
import io.miragon.bpmrepo.core.artifact.api.mapper.DeploymentApiMapper;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactMilestoneTO;
import io.miragon.bpmrepo.core.artifact.api.transport.DeploymentTO;
import io.miragon.bpmrepo.core.artifact.api.transport.NewDeploymentTO;
import io.miragon.bpmrepo.core.artifact.domain.facade.DeploymentFacade;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
@Transactional
@RestController
@RequiredArgsConstructor
@Tag(name = "Deployment")
@RequestMapping("/api/deploy")
public class ArtifactMilestoneDeploymentController {

    private final UserService userService;
    private final ArtifactMilestoneApiMapper milestoneApiMapper;
    private final DeploymentApiMapper deploymentApiMapper;
    private final DeploymentFacade deploymentFacade;

    /**
     * Deploy a specific milestone of an artifact
     *
     * @param newDeploymentTO deployment object containing ids and target
     * @return the artifactmilestone containing the list of deployments
     */
    @Operation(summary = "Deploy artifact milestone")
    @PostMapping()
    public ResponseEntity<ArtifactMilestoneTO> deployMilestone(@RequestBody final NewDeploymentTO newDeploymentTO) {
        log.debug("Deploying artifact milestone {}", newDeploymentTO.getMilestoneId());
        System.out.println("in methods");
        final User user = this.userService.getCurrentUser();
        final ArtifactMilestone artifactMilestone = this.deploymentFacade.deploy(this.deploymentApiMapper.mapToModel(newDeploymentTO), user);
        return ResponseEntity.ok().body(this.milestoneApiMapper.mapToTO(artifactMilestone));
    }

    /**
     * Deploy milestones of multiple artifacts (Throws an error if one of the milestones has already been deployed to the provided target. To change the behaviour, edit the "createOrUpdateDeployment" Method in ArtifactMilestoneDeploymentService
     *
     * @param deployments list of deployment objects containing ids and target
     * @return the list of artifactmilestones containing their new deployments
     */
    @Operation(summary = "Deploy multiple milestones")
    @PostMapping("/list")
    public ResponseEntity<List<ArtifactMilestoneTO>> deployMultipleMilestones(@RequestBody final List<NewDeploymentTO> deployments) {
        log.debug("Deploying {} artifact milestones", deployments.size());
        final User user = this.userService.getCurrentUser();
        final List<ArtifactMilestone> artifactMilestones = this.deploymentFacade.deployMultiple(this.deploymentApiMapper.mapToModel(deployments), user);
        return ResponseEntity.ok().body(this.milestoneApiMapper.mapToTO(artifactMilestones));
    }

    /**
     * Get all deployments of a repository
     *
     * @param repositoryId id of the repository
     * @return a List of DeploymentTOs
     */
    @Operation(summary = "Get all deployments of a repository")
    @GetMapping("/repository/{repositoryId}")
    public ResponseEntity<List<DeploymentTO>> getAllDeploymentsFromRepository(@PathVariable @NotBlank final String repositoryId) {
        log.debug("Returning all deployments of repository {}", repositoryId);
        final List<Deployment> deployments = this.deploymentFacade.getAllDeploymentsFromRepository(repositoryId);
        return ResponseEntity.ok().body(this.deploymentApiMapper.mapToTO(deployments));
    }

    /**
     * Get all available deployment targets
     *
     * @return available deployment targets as list of strings
     */
    @Operation(summary = "Get all available deployment targets")
    @GetMapping("/target")
    public ResponseEntity<List<String>> getAllDeploymentTargets() {
        log.debug("Returning all deployment targets");
        final List<String> deploymentTargets = this.deploymentFacade.getDeploymentTargets();
        return ResponseEntity.ok(deploymentTargets);
    }

}
