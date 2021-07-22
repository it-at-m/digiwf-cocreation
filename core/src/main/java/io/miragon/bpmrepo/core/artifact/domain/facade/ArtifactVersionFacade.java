package io.miragon.bpmrepo.core.artifact.domain.facade;

import io.miragon.bpmrepo.core.artifact.domain.business.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.business.ArtifactVersionService;
import io.miragon.bpmrepo.core.artifact.domain.business.LockService;
import io.miragon.bpmrepo.core.artifact.domain.business.VerifyRelationService;
import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersionUpload;
import io.miragon.bpmrepo.core.repository.domain.business.AuthService;
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

    public String createOrUpdateVersion(final String artifactId, final ArtifactVersionUpload artifactVersionUpload) {
        //TODO refactoring - to complicated

        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);

        final ArtifactVersion artifactVersion = ArtifactVersion.builder()
                .repositoryId(artifact.getRepositoryId())
                .artifactId(artifactId)
                .comment(artifactVersionUpload.getVersionComment())
                .xml(artifactVersionUpload.getXml())
                .saveType(artifactVersionUpload.getSaveType())
                .updatedDate(LocalDateTime.now())
                .build();

        //initial version
        if (this.verifyRelationService.checkIfVersionIsInitialVersion(artifactId)) {
            final String bpmnArtifactVersionId = this.artifactVersionService.createInitialVersion(artifactVersion);
            this.artifactService.updateUpdatedDate(artifactId);
            return bpmnArtifactVersionId;
        }


        //Update current version
        if (artifactVersionUpload.getSaveType() == SaveTypeEnum.AUTOSAVE) {
            this.lockService.checkIfVersionIsUnlockedOrLockedByActiveUser(artifact);
            final String bpmnArtifactVersionId = this.artifactVersionService.updateVersion(artifactVersion);
            //refresh the updated date in artifactEntity
            this.artifactService.updateUpdatedDate(artifactId);
            return bpmnArtifactVersionId;
        }

        //Create new Version
        log.warn("Creating new version");
        this.lockService.checkIfVersionIsUnlockedOrLockedByActiveUser(artifact);
        final String bpmnArtifactVersionId = this.artifactVersionService.createNewVersion(artifactVersion);
        this.artifactService.updateUpdatedDate(artifactId);
        this.deleteAutosavedVersionsIfMilestoneIsSaved(artifact.getRepositoryId(), artifactId, artifactVersionUpload.getSaveType());
        return bpmnArtifactVersionId;
    }

    //simply deletes all entities that contain the SaveType "AUTOSAVE"
    private void deleteAutosavedVersionsIfMilestoneIsSaved(final String bpmnRepositoryId, final String bpmnartifactId,
                                                           final SaveTypeEnum saveTypeEnum) {
        if (saveTypeEnum.equals(SaveTypeEnum.AUTOSAVE)) {
            this.artifactVersionService.deleteAutosavedVersions(bpmnRepositoryId, bpmnartifactId);
        }
    }

    public List<ArtifactVersion> getAllVersions(final String artifactId) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        return this.artifactVersionService.getAllVersions(artifactId);
    }

    public ArtifactVersion getLatestVersion(final String artifactId) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        return this.artifactVersionService.getLatestVersion(artifactId);
    }

    public ArtifactVersion getVersion(final String artifactId, final String artifactVersionId) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        this.lockService.checkIfVersionIsUnlockedOrLockedByActiveUser(artifact);
        return this.artifactVersionService.getVersion(artifactVersionId);
    }

    public ByteArrayResource downloadVersion(final String artifactId, final String artifactVersionId) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        return this.artifactVersionService.downloadVersion(artifact.getName(), artifactVersionId);
    }

    public HttpHeaders getHeaders(final String artifactId) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        final String fileName = String.format("%s.%s", artifact.getName(), artifact.getFileType());

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


