package io.miragon.bpmrepo.core.artifact.domain.facade;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTypeTO;
import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersionUpdate;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersionUpload;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactVersionService;
import io.miragon.bpmrepo.core.artifact.domain.service.LockService;
import io.miragon.bpmrepo.core.artifact.domain.service.VerifyRelationService;
import io.miragon.bpmrepo.core.artifact.plugin.ArtifactTypesPlugin;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArtifactVersionFacade {
    private final AuthService authService;
    private final LockService lockService;
    private final VerifyRelationService verifyRelationService;
    private final ArtifactVersionService artifactVersionService;
    private final ArtifactService artifactService;

    private final ArtifactTypesPlugin artifactTypesPlugin;


    public ArtifactVersion createVersion(final String artifactId, final ArtifactVersionUpload artifactVersionUpload) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        final ArtifactVersion artifactVersion = ArtifactVersion.builder()
                .repositoryId(artifact.getRepositoryId())
                .artifactId(artifactId)
                .comment(artifactVersionUpload.getComment())
                .xml(artifactVersionUpload.getXml())
                .saveType(artifactVersionUpload.getSaveType())
                .updatedDate(LocalDateTime.now())
                .latestVersion(true)
                .build();

        //initial version
        if (this.verifyRelationService.checkIfVersionIsInitialVersion(artifactId)) {
            final ArtifactVersion createdArtifactVersion = this.artifactVersionService.createInitialVersion(artifactVersion);
            this.artifactService.updateUpdatedDate(artifactId);
            return createdArtifactVersion;
        }

        //Create new Version - additional Check for Locking
        this.lockService.checkIfVersionIsUnlockedOrLockedByActiveUser(artifact);
        final ArtifactVersion oldArtifactVersion = this.artifactVersionService.getLatestVersion(artifactId);
        this.artifactVersionService.setVersionOutdated(oldArtifactVersion);
        final ArtifactVersion createdArtifactVersion = this.artifactVersionService.createNewVersion(artifactVersion);
        this.artifactService.updateUpdatedDate(artifactId);
        this.deleteAutosavedVersionsIfMilestoneIsSaved(artifact.getRepositoryId(), artifactId, artifactVersionUpload.getSaveType());
        return createdArtifactVersion;
    }


    public ArtifactVersion updateVersion(final String artifactId, final ArtifactVersionUpdate artifactVersionUpdate) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        final ArtifactVersion requestedArtifactVersion = this.artifactVersionService.getVersion(artifactVersionUpdate.getVersionId());
        final ArtifactVersion latestVersion = this.artifactVersionService.getLatestVersion(artifactId);
        if (!requestedArtifactVersion.getId().equals(latestVersion.getId())) {
            //TODO: Throw custom error "Cant edit historical data"
            throw new RuntimeException();
        }

        return this.artifactVersionService.updateVersion(artifactVersionUpdate);
    }


    //deletes all entities that contain the SaveType "AUTOSAVE"
    private void deleteAutosavedVersionsIfMilestoneIsSaved(final String repositoryId, final String artifactId,
                                                           final SaveTypeEnum saveTypeEnum) {
        if (saveTypeEnum.equals(SaveTypeEnum.MILESTONE)) {
            this.artifactVersionService.deleteAutosavedVersions(repositoryId, artifactId);
        }
    }

    public List<ArtifactVersion> getAllVersions(final String artifactId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        return this.artifactVersionService.getAllVersions(artifactId);
    }

    public ArtifactVersion getLatestVersion(final String artifactId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        return this.artifactVersionService.getLatestVersion(artifactId);
    }

    public ArtifactVersion getVersion(final String artifactId, final String artifactVersionId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        this.lockService.checkIfVersionIsUnlockedOrLockedByActiveUser(artifact);
        return this.artifactVersionService.getVersion(artifactVersionId);
    }

    public ByteArrayResource downloadVersion(final String artifactId, final String artifactVersionId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        return this.artifactVersionService.downloadVersion(artifactVersionId);
    }

    public HttpHeaders getHeaders(final String artifactId) {
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        final List<ArtifactTypeTO> artifactTypes = this.artifactTypesPlugin.getArtifactTypes();
        final ArtifactTypeTO artifactType = artifactTypes.stream().filter(type -> type.getName().equals(artifact.getFileType())).findFirst().orElseThrow();
        final String fileName = String.format("%s.%s", artifact.getName(), artifactType.getFileExtension());

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; fileName=%s", fileName));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return headers;
    }
}


