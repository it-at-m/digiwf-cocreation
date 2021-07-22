package io.miragon.bpmrepo.core.artifact.api.resource;

import io.miragon.bpmrepo.core.artifact.api.transport.DeploymentTO;
import io.miragon.bpmrepo.core.artifact.domain.business.ArtifactVersionDeploymentService;
import io.miragon.bpmrepo.core.security.UserContext;
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
    private final UserContext userContext;

    @Operation(description = "Deploy artifact version")
    @PostMapping("/{artifactId}/{versionId}")
    public ResponseEntity<Void> deployVersion(
            @PathVariable final String artifactId,
            @PathVariable final String versionId,
            @RequestBody final DeploymentTO deployment) {
        this.deploymentService.deploy(artifactId, versionId, deployment.getTarget());
        return ResponseEntity.ok().build();

    }

    @Operation(description = "Get all available deployment targets")
    @GetMapping("/target")
    public ResponseEntity<List<String>> getAllDeploymentTargets() {
        final List<String> deploymentTargets = this.deploymentService.getDeploymentTargets();
        return ResponseEntity.ok(deploymentTargets);
    }


}
