package io.miragon.bpmrepo.core.artifact.domain.facade;

import io.miragon.bpmrepo.core.artifact.domain.model.*;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactMilestoneService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.LockService;
import io.miragon.bpmrepo.core.artifact.domain.service.StarredService;
import io.miragon.bpmrepo.core.repository.domain.service.AssignmentService;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.repository.domain.service.RepositoryService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ArtifactMilestoneFacade artifactMilestoneFacade;

    private final ArtifactService artifactService;
    private final ArtifactMilestoneService artifactMilestoneService;
    private final StarredService starredService;

    private final AssignmentService assignmentService;
    private final RepositoryService repositoryService;

    public Artifact createArtifact(final String repositoryId, final Artifact artifact, final String file) {
        log.debug("Create artifact");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.MEMBER);
        artifact.updateRepositoryId(repositoryId);
        final Artifact createdArtifact = this.artifactService.createArtifact(artifact);
        final ArtifactMilestoneUpload milestone = new ArtifactMilestoneUpload("", file != null ? file : "");
        this.artifactMilestoneFacade.createMilestone(createdArtifact.getId(), milestone);
        final Integer existingArtifacts = this.artifactService.countExistingArtifacts(repositoryId);
        this.repositoryService.updateExistingArtifacts(repositoryId, existingArtifacts);
        return createdArtifact;
    }

    public Artifact updateArtifact(final String artifactId, final ArtifactUpdate artifactUpdate) {
        log.debug("Update artifact");
        Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        artifact = this.artifactService.updateArtifact(artifact, artifactUpdate);

        // update latest milestone if file is present
        if (artifactUpdate.getFile() != null) {
            // lock artifact before save
            final String currentUsersName = this.userService.getCurrentUser().getUsername();
            this.lockArtifact(artifact.getId(), currentUsersName);

            final ArtifactMilestone artifactMilestone = this.artifactMilestoneService.getLatestMilestone(artifact.getId());
            final ArtifactMilestoneUpdate milestone = new ArtifactMilestoneUpdate(artifactMilestone.getId(), "", artifactUpdate.getFile());
            this.artifactMilestoneFacade.updateMilestone(milestone);

            // and unlock artifact after save
            this.unlockArtifact(artifact.getId());
        }

        return artifact;
    }

    public List<Artifact> getArtifactsFromRepo(final String repositoryId) {
        log.debug("Get artifact from repo");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.artifactService.getArtifactsByRepo(repositoryId);
    }


    public Artifact getArtifact(final String artifactId) {
        log.debug("get artifact");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER, artifactId);
        return artifact;
    }

    public List<Artifact> getRecent(final String userId) {
        log.debug("Get recent artifacts");
        final List<String> assignedRepositoryIds = this.assignmentService.getAllAssignedRepositoryIds(userId);
        return this.artifactService.getRecent(assignedRepositoryIds);
    }

    public void deleteArtifact(final String artifactId) {
        log.debug("delete artifact");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        this.artifactMilestoneService.deleteAllByArtifactId(artifactId);
        this.artifactService.deleteArtifact(artifactId);
        final Integer existingArtifacts = this.artifactService.countExistingArtifacts(artifact.getRepositoryId());
        this.repositoryService.updateExistingArtifacts(artifact.getRepositoryId(), existingArtifacts);
    }

    public void setStarred(final String artifactId, final String userId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.VIEWER);
        this.starredService.setStarred(artifactId, userId);
    }

    public List<Artifact> getStarred(final String userId) {
        log.debug("Searching for starred-relations");
        final List<String> artifactIds = this.starredService.getStarred(userId).stream().map(starredEntity -> starredEntity.getId().getArtifactId()).collect(Collectors.toList());
        return this.artifactService.getAllArtifactsById(artifactIds);
    }

    public List<Artifact> searchArtifacts(final String typedTitle, final String userId) {
        log.debug("Searching in all assigned Artifacts");
        final List<String> assignedRepoIds = this.assignmentService.getAllAssignedRepositoryIds(userId);
        return this.artifactService.searchArtifacts(assignedRepoIds, typedTitle);
    }

    public Artifact lockArtifact(final String artifactId, final String username) {
        log.debug("lock artifact {}", artifactId);
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(artifact);
        return this.artifactService.lockArtifact(artifactId, username);
    }

    public Artifact unlockArtifact(final String artifactId) {
        log.debug("unlock artifact {}", artifactId);
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(artifact);
        return this.artifactService.unlockArtifact(artifactId);
    }

    public Artifact copyToRepository(final String repositoryId, final String artifactId, final String title, final String description) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));

        //check permissions
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.MEMBER);
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.MEMBER);

        final ArtifactMilestone artifactMilestone = this.artifactMilestoneService.getLatestMilestone(artifactId);
        final Artifact newArtifact = new Artifact();
        newArtifact.copyFrom(artifact, repositoryId, title, description);
        final ArtifactMilestoneUpload newArtifactVersion = new ArtifactMilestoneUpload();
        newArtifactVersion.setFile(artifactMilestone.getFile());
        final Artifact createdArtifact = this.artifactService.createArtifact(newArtifact);
        this.artifactMilestoneFacade.createMilestone(createdArtifact.getId(), newArtifactVersion);
        return createdArtifact;
    }

    public List<Artifact> getByRepoIdAndType(final String repositoryId, final String type) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.artifactService.getByRepoIdAndType(repositoryId, type);
    }
}
