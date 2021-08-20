package io.miragon.bpmrepo.core.artifact.api.resource;

import io.miragon.bpmrepo.core.artifact.api.mapper.ArtifactVersionApiMapper;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactVersionTO;
import io.miragon.bpmrepo.core.artifact.api.transport.NewDeploymentTO;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactVersionDeploymentService;
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
import java.util.List;

@Slf4j
@Validated
@Transactional
@RestController
@RequiredArgsConstructor
@Tag(name = "Deployment")
@RequestMapping("/api/deploy")
public class ArtifactVersionDeploymentController {

    private final ArtifactVersionDeploymentService deploymentService;
    private final UserService userService;
    private final ArtifactVersionApiMapper apiMapper;

    /**
     * Deploy a specific version of an artifact
     *
     * @param artifactId Id of the artifact to be deployed
     * @param versionId  Id of the version to be deployed
     * @param deployment deployment object containing the target
     * @return the artifactversion containing the list of deployments
     */
    @Operation(description = "Deploy artifact version")
    @PostMapping("/{artifactId}/{versionId}")
    public ResponseEntity<ArtifactVersionTO> deployVersion(
            @PathVariable final String artifactId,
            @PathVariable final String versionId,
            @RequestBody final NewDeploymentTO deployment) {
        log.debug("Deploying artifact version {}", versionId);
        final User user = this.userService.getCurrentUser();
        final ArtifactVersion artifactVersion = this.deploymentService.deploy(artifactId, versionId, deployment.getTarget(), user);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(artifactVersion));

    }

    /**
     * Get all available deployment targets
     *
     * @return available deployment targets as list of strings
     */
    @Operation(description = "Get all available deployment targets")
    @GetMapping("/target")
    public ResponseEntity<List<String>> getAllDeploymentTargets() {
        log.debug("Returning all deployment targets");
        final List<String> deploymentTargets = this.deploymentService.getDeploymentTargets();
        return ResponseEntity.ok(deploymentTargets);
    }

}
