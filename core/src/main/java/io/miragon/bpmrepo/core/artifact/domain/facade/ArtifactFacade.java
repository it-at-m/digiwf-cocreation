package io.miragon.bpmrepo.core.artifact.domain.facade;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactUpdate;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersionUpload;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactVersionService;
import io.miragon.bpmrepo.core.artifact.domain.service.LockService;
import io.miragon.bpmrepo.core.artifact.domain.service.StarredService;
import io.miragon.bpmrepo.core.repository.domain.service.AssignmentService;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.repository.domain.service.RepositoryService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArtifactFacade {
    private final AuthService authService;
    private final LockService lockService;

    private final ArtifactVersionFacade artifactVersionFacade;

    private final ArtifactService artifactService;
    private final ArtifactVersionService artifactVersionService;
    private final StarredService starredService;

    private final AssignmentService assignmentService;
    private final RepositoryService repositoryService;

    public Artifact createArtifact(final String repositoryId, final Artifact artifact) {
        log.debug("Checking permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.MEMBER);
        artifact.updateRepositoryId(repositoryId);
        final Artifact createdArtifact = this.artifactService.createArtifact(artifact);
        final Integer existingArtifacts = this.artifactService.countExistingArtifacts(repositoryId);
        this.repositoryService.updateExistingArtifacts(repositoryId, existingArtifacts);
        return createdArtifact;
    }

    public Artifact updateArtifact(final String artifactId, final ArtifactUpdate artifactUpdate) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        return this.artifactService.updateArtifact(artifact, artifactUpdate);
    }

    public Optional<List<Artifact>> getArtifactsFromRepo(final String repositoryId) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.artifactService.getArtifactsByRepo(repositoryId);
    }

    public Artifact getArtifact(final String artifactId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        return artifact;
    }

    public Optional<List<Artifact>> getRecent(final String userId) {
        log.debug("Checking Assignments");
        final List<String> assignedRepositoryIds = this.assignmentService.getAllAssignedRepositoryIds(userId);
        return this.artifactService.getRecent(assignedRepositoryIds);
    }

    public Artifact updatePreviewSVG(final String artifactId, final String svgPreview) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        return this.artifactService.updatePreviewSVG(artifactId, svgPreview);
    }

    public void deleteArtifact(final String artifactId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        this.artifactVersionService.deleteAllByArtifactId(artifactId);
        this.artifactService.deleteArtifact(artifactId);
        final Integer existingArtifacts = this.artifactService.countExistingArtifacts(artifact.getRepositoryId());
        this.repositoryService.updateExistingArtifacts(artifact.getRepositoryId(), existingArtifacts);
    }

    public void setStarred(final String artifactId, final String userId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        this.starredService.setStarred(artifactId, userId);
    }

    public Optional<List<Artifact>> getStarred(final String userId) {
        log.debug("Searching for starred-relations");
        final List<String> artifactIds = this.starredService.getStarred(userId).stream().map(starredEntity -> starredEntity.getId().getArtifactId()).collect(Collectors.toList());
        return this.artifactService.getAllArtifactsById(artifactIds);
    }

    public Optional<List<Artifact>> searchArtifacts(final String typedTitle, final String userId) {
        log.debug("Searching in all assigned Artifacts");
        final List<String> assignedRepoIds = this.assignmentService.getAllAssignedRepositoryIds(userId);
        return this.artifactService.searchArtifacts(assignedRepoIds, typedTitle);
    }

    public Artifact lockArtifact(final String artifactId, final String username) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        this.lockService.checkIfVersionIsUnlockedOrLockedByActiveUser(artifact);
        return this.artifactService.lockArtifact(artifactId, username);
    }

    public Artifact unlockArtifact(final String artifactId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        this.lockService.checkIfVersionIsUnlockedOrLockedByActiveUser(artifact);
        return this.artifactService.unlockArtifact(artifactId);
    }

    public Artifact copyToRepository(final String repositoryId, final String artifactId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        final ArtifactVersion artifactVersion = this.artifactVersionService.getLatestVersion(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.MEMBER);
        final Artifact newArtifact = new Artifact();
        newArtifact.copy(artifact);
        newArtifact.setRepositoryId(repositoryId);

        final ArtifactVersionUpload newArtifactVersion = new ArtifactVersionUpload();
        newArtifactVersion.setXml(artifactVersion.getXml());
        newArtifactVersion.setSaveType(SaveTypeEnum.MILESTONE);

        final Artifact createdArtifact = this.artifactService.createArtifact(newArtifact);
        this.artifactVersionFacade.createVersion(createdArtifact.getId(), newArtifactVersion);
        return createdArtifact;
    }

    public Optional<List<Artifact>> getByRepoIdAndType(final String repositoryId, final String type) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.artifactService.getByRepoIdAndType(repositoryId, type);
    }
}
