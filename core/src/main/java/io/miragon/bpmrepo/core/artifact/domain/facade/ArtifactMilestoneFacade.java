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
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        final ArtifactMilestone artifactMilestone = new ArtifactMilestone(artifactId, artifact.getRepositoryId(), artifactMilestoneUpload.getFile(), artifactMilestoneUpload.getComment());

        //initial version
        if (this.verifyRelationService.checkIfMilestoneIsInitialMilestone(artifactId)) {
            return this.saveMilestone(artifactId, this.artifactMilestoneService.createInitialMilestone(artifactMilestone));
        }

        //Create new Milestone - additional Check for Locking
        this.outdateLatestMilestone(artifactId);
        return this.saveMilestone(artifactId, this.artifactMilestoneService.createNewMilestone(artifactMilestone));
    }

    public ArtifactMilestone updateMilestone(final ArtifactMilestoneUpdate artifactMilestoneUpdate) {
        log.debug("Checking permissions");
        final ArtifactMilestone artifactMilestone = this.artifactMilestoneService.getMilestone(artifactMilestoneUpdate.getMilestoneId())
                .orElseThrow(() -> new ObjectNotFoundException("exception.milestoneNotFound"));
        final Artifact artifact = this.artifactService.getArtifactById(artifactMilestone.getArtifactId()).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifactMilestone.getRepositoryId(), RoleEnum.MEMBER);
        this.lockService.checkIfMilestoneIsLockedByActiveUser(artifact);

        final ArtifactMilestone latestMilestone = this.artifactMilestoneService.getLatestMilestone(artifact.getId());
        if (!artifactMilestone.getId().equals(latestMilestone.getId())) {
            throw new HistoricalDataAccessException("exception.historicalDataAccess");
        }
//        TODO think about allowing it only for deployments in the test stage
//        if (artifactMilestone.getDeployments().size() > 0) {
//            throw new AlreadyDeployedException("exception.alreadyDeployed");
//        }

        return this.artifactMilestoneService.updateMilestone(artifactMilestoneUpdate);
    }

    public List<ArtifactMilestone> getAllMilestones(final String artifactId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER, artifactId);
        return this.artifactMilestoneService.getAllMilestones(artifactId);
    }

    public ArtifactMilestone getLatestMilestone(final String artifactId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER, artifactId);
        return this.artifactMilestoneService.getLatestMilestone(artifactId);
    }

    public Optional<ArtifactMilestone> getByMilestoneNumber(final String artifactId, final Integer milestoneNumber) {
        log.debug("Checking Permission");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER, artifactId);
        return this.artifactMilestoneService.getByMilestoneNumber(artifactId, milestoneNumber);
    }

    public Optional<ArtifactMilestone> getMilestone(final String artifactId, final String artifactMilestoneId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER, artifactId);
        return this.artifactMilestoneService.getMilestone(artifactMilestoneId);
    }

    public ByteArrayResource downloadMilestone(final String artifactId, final String artifactMilestoneId) {
        log.debug("Checking permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        return this.artifactMilestoneService.downloadMilestone(artifactMilestoneId);
    }

    public HttpHeaders getHeaders(final String artifactId) {
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
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

    //------------------------------ HELPER METHODS ------------------------------//

    private ArtifactMilestone saveMilestone(final String artifactId, final ArtifactMilestone initialMilestone) {
        this.artifactService.updatedDate(artifactId);
        return initialMilestone;
    }

    private void outdateLatestMilestone(final String artifactId) {
        final ArtifactMilestone oldArtifactMilestone = this.artifactMilestoneService.getLatestMilestone(artifactId);
        this.artifactMilestoneService.setMilestoneOutdated(oldArtifactMilestone);
    }
}


