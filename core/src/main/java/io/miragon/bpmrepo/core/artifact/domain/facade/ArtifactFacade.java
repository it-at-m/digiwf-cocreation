package io.miragon.bpmrepo.core.artifact.domain.facade;

import io.miragon.bpmrepo.core.artifact.domain.business.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.business.ArtifactVersionService;
import io.miragon.bpmrepo.core.artifact.domain.business.LockService;
import io.miragon.bpmrepo.core.artifact.domain.business.StarredService;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactUpdate;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.StarredEntity;
import io.miragon.bpmrepo.core.repository.domain.business.AssignmentService;
import io.miragon.bpmrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmrepo.core.repository.domain.business.RepositoryService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArtifactFacade {
    private final AuthService authService;
    private final LockService lockService;
    private final UserService userService;

    private final ArtifactService artifactService;
    private final ArtifactVersionService artifactVersionService;
    private final StarredService starredService;

    private final AssignmentService assignmentService;
    private final RepositoryService repositoryService;

    public Artifact createArtifact(final String repositoryId, final Artifact artifact) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.MEMBER);
        artifact.updateRepositoryId(repositoryId);
        val result = this.artifactService.createArtifact(artifact);
        final Integer existingArtifacts = this.artifactService.countExistingArtifacts(repositoryId);
        this.repositoryService.updateExistingArtifacts(repositoryId, existingArtifacts);
        log.debug("Artifact created");
        return result;
    }

    public Artifact updateArtifact(final String artifactId, final ArtifactUpdate artifactUpdate) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        val result = this.artifactService.updateArtifact(artifactId, artifactUpdate);
        log.debug("Artifact updated");
        return result;
    }

    public List<Artifact> getArtifactsFromRepo(final String repositoryId) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.artifactService.getArtifactsByRepo(repositoryId);
    }

    public Artifact getArtifact(final String artifactId) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        return artifact;
    }

    public List<Artifact> getRecent() {
        final List<String> assignments = this.assignmentService.getAllAssignedRepositoryIds(this.userService.getUserIdOfCurrentUser());
        return this.artifactService.getRecent(assignments);
    }

    public void updatePreviewSVG(final String artifactId, final String svgPreview) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        this.artifactService.updatePreviewSVG(artifactId, svgPreview);
    }

    public void deleteArtifact(final String artifactId) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        this.artifactVersionService.deleteAllByArtifactId(artifactId);
        this.artifactService.deleteArtifact(artifactId);
        final Integer existingArtifacts = this.artifactService.countExistingArtifacts(artifact.getRepositoryId());
        this.repositoryService.updateExistingArtifacts(artifact.getRepositoryId(), existingArtifacts);
    }

    public void setStarred(final String artifactId, final String userId) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        this.starredService.setStarred(artifactId, userId);
    }

    public List<Artifact> getStarred() {
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final List<StarredEntity> starredList = this.starredService.getStarred(currentUserId);
        return starredList.stream()
                .map(starredEntity -> this.artifactService.getArtifactsById(starredEntity.getId().getArtifactId()))
                .collect(Collectors.toList());
    }

    public List<Artifact> searchArtifacts(final String typedTitle) {
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final List<String> assignedRepoIds = this.assignmentService.getAllAssignedRepositoryIds(currentUserId);
        final List<Artifact> artifactList = this.artifactService.searchArtifacts(assignedRepoIds, typedTitle);
        return artifactList;
    }

    public void lockArtifact(final String artifactId) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        System.out.println(artifact.getRepositoryId());
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        this.lockService.checkIfVersionIsUnlockedOrLockedByActiveUser(artifact);
        this.artifactService.lockArtifact(artifactId, this.userService.getCurrentUser().getUsername());
    }

    public void unlockArtifact(final String artifactId) {
        final Artifact artifact = this.artifactService.getArtifactsById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        this.lockService.checkIfVersionIsUnlockedOrLockedByActiveUser(artifact);
        this.artifactService.unlockArtifact(artifactId);
    }

}
