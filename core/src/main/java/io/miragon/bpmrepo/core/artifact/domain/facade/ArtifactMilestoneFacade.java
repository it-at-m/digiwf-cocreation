package io.miragon.bpmrepo.core.artifact.domain.facade;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTypeTO;
import io.miragon.bpmrepo.core.artifact.domain.exception.HistoricalDataAccessException;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestoneUpdate;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestoneUpload;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactMilestoneService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.LockService;
import io.miragon.bpmrepo.core.artifact.domain.service.VerifyRelationService;
import io.miragon.bpmrepo.core.artifact.plugin.ArtifactTypesPlugin;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArtifactMilestoneFacade {
    private final AuthService authService;
    private final LockService lockService;
    private final VerifyRelationService verifyRelationService;
    private final ArtifactMilestoneService artifactMilestoneService;
    private final ArtifactService artifactService;

    private final ArtifactTypesPlugin artifactTypesPlugin;


    public ArtifactMilestone createMilestone(final String artifactId, final ArtifactMilestoneUpload artifactMilestoneUpload) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        final ArtifactMilestone artifactMilestone = ArtifactMilestone.builder()
                .repositoryId(artifact.getRepositoryId())
                .artifactId(artifactId)
                .comment(artifactMilestoneUpload.getComment())
                .file(artifactMilestoneUpload.getFile())
                .updatedDate(LocalDateTime.now())
                .latestMilestone(true)
                .build();

        //initial version
        if (this.verifyRelationService.checkIfMilestoneIsInitialMilestone(artifactId)) {
            final ArtifactMilestone createdArtifactMilestone = this.artifactMilestoneService.createInitialMilestone(artifactMilestone);
            this.artifactService.updateUpdatedDate(artifactId);
            return createdArtifactMilestone;
        }

        //Create new Milestone - additional Check for Locking
        this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(artifact);
        final ArtifactMilestone oldArtifactMilestone = this.artifactMilestoneService.getLatestMilestone(artifactId);
        this.artifactMilestoneService.setMilestoneOutdated(oldArtifactMilestone);
        final ArtifactMilestone createdArtifactMilestone = this.artifactMilestoneService.createNewMilestone(artifactMilestone);
        this.artifactService.updateUpdatedDate(artifactId);
        return createdArtifactMilestone;
    }


    public ArtifactMilestone updateMilestone(final ArtifactMilestoneUpdate artifactMilestoneUpdate) {
        log.debug("Checking permissions");
        final Optional<ArtifactMilestone> artifactMilestoneOpt = this.artifactMilestoneService.getMilestone(artifactMilestoneUpdate.getMilestoneId());
        if (artifactMilestoneOpt.isEmpty()) {
            throw new ObjectNotFoundException("exception.milestoneNotFound");
        }
        final ArtifactMilestone artifactMilestone = artifactMilestoneOpt.get();
        final Artifact artifact = this.artifactService.getArtifactById(artifactMilestone.getArtifactId());
        this.authService.checkIfOperationIsAllowed(artifactMilestone.getRepositoryId(), RoleEnum.MEMBER);
        this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(artifact);
        final ArtifactMilestone latestMilestone = this.artifactMilestoneService.getLatestMilestone(artifact.getId());
        if (!artifactMilestone.getId().equals(latestMilestone.getId())) {
            throw new HistoricalDataAccessException("exception.historicalDataAccess");
        }

        return this.artifactMilestoneService.updateMilestone(artifactMilestoneUpdate);
    }


    public List<ArtifactMilestone> getAllMilestones(final String artifactId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER, artifactId);
        return this.artifactMilestoneService.getAllMilestones(artifactId);
    }

    public ArtifactMilestone getLatestMilestone(final String artifactId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER, artifactId);
        return this.artifactMilestoneService.getLatestMilestone(artifactId);
    }

    public Optional<ArtifactMilestone> getByMilestoneNumber(final String artifactId, final Integer milestoneNumber) {
        log.debug("Checking Permission");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER, artifactId);
        this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(artifact);
        return this.artifactMilestoneService.getByMilestoneNumber(artifactId, milestoneNumber);
    }


    public Optional<ArtifactMilestone> getMilestone(final String artifactId, final String artifactMilestoneId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER, artifactId);
        this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(artifact);
        return this.artifactMilestoneService.getMilestone(artifactMilestoneId);
    }

    public ByteArrayResource downloadMilestone(final String artifactId, final String artifactMilestoneId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        return this.artifactMilestoneService.downloadMilestone(artifactMilestoneId);
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

    public List<ArtifactMilestone> getAllByDeploymentIds(final List<String> deploymentIds) {
        log.debug("Checking permissions");
        final List<ArtifactMilestone> milestones = this.artifactMilestoneService.getAllByDeploymentIds(deploymentIds);
        milestones.forEach(milestone -> this.authService.checkIfOperationIsAllowed(milestone.getRepositoryId(), RoleEnum.VIEWER));
        return milestones;
    }
}


