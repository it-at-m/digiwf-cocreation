package io.miragon.bpmrepo.core.artifact.api.resource;

import io.miragon.bpmrepo.core.artifact.api.mapper.ArtifactVersionApiMapper;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactVersionTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactVersionUploadTO;
import io.miragon.bpmrepo.core.artifact.domain.facade.ArtifactVersionFacade;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@Transactional
@Validated
@RequiredArgsConstructor
@Tag(name = "Version")
@RequestMapping("api/version")
public class ArtifactVersionController {

    private final ArtifactVersionFacade artifactVersionFacade;
    private final ArtifactVersionApiMapper apiMapper;

    /**
     * Create a new version of the artifact. No Write permission if artifact is not locked by requesting user
     *
     * @param artifactId              Id of the artifact
     * @param artifactVersionUploadTO Update object
     */
    @PostMapping("/{artifactId}")
    public ResponseEntity<Void> createOrUpdateVersion(
            @PathVariable @NotBlank final String artifactId,
            @RequestBody @Valid final ArtifactVersionUploadTO artifactVersionUploadTO) {
        log.warn("Creating new Version. Savetype: " + artifactVersionUploadTO.getSaveType());
        final String artifactVersionId = this.artifactVersionFacade.createOrUpdateVersion(artifactId, this.apiMapper.mapUploadToModel(artifactVersionUploadTO));
        log.warn(String.format("Current versionId: %s", artifactVersionId));
        return ResponseEntity.ok().build();
    }

    /**
     * Get latest version
     *
     * @param artifactId Id of the artifact
     * @return latest version
     */
    @GetMapping("/{artifactId}/latest")
    @Operation(summary = "Return the latest version of the requested artifact")
    public ResponseEntity<ArtifactVersionTO> getLatestVersion(@PathVariable @NotBlank final String artifactId) {
        log.debug("Returning latest version");
        final ArtifactVersion latestVersion = this.artifactVersionFacade.getLatestVersion(artifactId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(latestVersion));
    }

    /**
     * Get all versions of the artifact
     *
     * @param artifactId Id of the artifact
     * @return all versions
     */
    @GetMapping("/{artifactId}/all")
    public ResponseEntity<List<ArtifactVersionTO>> getAllVersions(@PathVariable @NotBlank final String artifactId) {
        log.debug("Returning all Versions");
        val versions = this.artifactVersionFacade.getAllVersions(artifactId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(versions));
    }

    /**
     * Get a specific version, read-permission granted even if Artifact is locked
     *
     * @param artifactId Id of the artifact
     * @param versionId  Id of the version
     * @return
     */
    @GetMapping("/{artifactId}/{versionId}")
    public ResponseEntity<ArtifactVersionTO> getVersion(
            @PathVariable @NotBlank final String artifactId,
            @PathVariable @NotBlank final String versionId) {
        log.debug("Returning single Version");
        final ArtifactVersion version = this.artifactVersionFacade.getVersion(artifactId, versionId);
        return ResponseEntity.ok(this.apiMapper.mapToTO(version));
    }

    /**
     * Download a specific version
     *
     * @param artifactId Id of the artifact
     * @param versionId  Id of the version
     * @return
     */
    @GetMapping("/{artifactId}/{versionId}/download")
    public ResponseEntity<Resource> downloadVersion(@PathVariable @NotBlank final String artifactId, @PathVariable @NotBlank final String versionId) {
        log.debug("Returning File for Download");
        final ByteArrayResource resource = this.artifactVersionFacade.downloadVersion(artifactId, versionId);
        final HttpHeaders headers = this.artifactVersionFacade.getHeaders(artifactId);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}