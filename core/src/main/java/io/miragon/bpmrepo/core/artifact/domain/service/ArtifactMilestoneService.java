package io.miragon.bpmrepo.core.artifact.domain.service;

import io.miragon.bpmrepo.core.artifact.domain.mapper.MilestoneMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestoneUpdate;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactMilestoneEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.ArtifactMilestoneJpaRepository;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtifactMilestoneService {

    private final ArtifactMilestoneJpaRepository artifactMilestoneJpaRepository;
    private final MilestoneMapper mapper;

    public ArtifactMilestone updateMilestone(final ArtifactMilestoneUpdate artifactMilestoneUpdate) {
        log.debug("Persisting version-update");
        final ArtifactMilestone updatedVersion = this.getMilestone(artifactMilestoneUpdate.getMilestoneId())
                .orElseThrow(() -> new ObjectNotFoundException("exception.versionNotFound"));
        updatedVersion.updateVersion(artifactMilestoneUpdate);
        return this.saveToDb(updatedVersion);
    }

    public void setMilestoneOutdated(final ArtifactMilestone artifactMilestone) {
        artifactMilestone.setOutdated();
        this.saveToDb(artifactMilestone);
    }

    public ArtifactMilestone createNewMilestone(final ArtifactMilestone artifactMilestone) {
        log.debug("Persisting new version");
        final ArtifactMilestone latestVersion = this.getLatestMilestone(artifactMilestone.getArtifactId());
        artifactMilestone.updateMilestoneNumber(latestVersion.getMilestone() + 1);
        return this.saveToDb(artifactMilestone);
    }

    public ArtifactMilestone createInitialMilestone(final ArtifactMilestone artifactMilestone) {
        log.debug("Persisting initial version");
        artifactMilestone.updateMilestoneNumber(1);
        return this.saveToDb(artifactMilestone);
    }

    public List<ArtifactMilestone> getAllMilestones(final String artifactId) {
        log.debug("Querying all versions");
        final List<ArtifactMilestoneEntity> artifactVersionEntities = this.artifactMilestoneJpaRepository.findAllByArtifactId(artifactId);
        return this.mapper.mapToModel(artifactVersionEntities);
    }

    public ArtifactMilestone getLatestMilestone(final String artifactId) {
        log.debug("Querying latest version");
        return this.artifactMilestoneJpaRepository
                .findFirstByArtifactIdOrderByMilestoneDesc(artifactId)
                .map(this.mapper::mapToModel)
                .orElseThrow();
    }

    public Optional<ArtifactMilestone> getByMilestoneNumber(final String artifactId, final Integer milestoneNumber) {
        log.debug("Querying specific milestone");
        return this.artifactMilestoneJpaRepository.findFirstByArtifactIdAndMilestoneOrderByUpdatedDateDesc(artifactId, milestoneNumber).map(this.mapper::mapToModel);
    }

    public Optional<ArtifactMilestone> getMilestone(final String artifactVersionId) {
        log.debug("Querying specific version");
        return this.artifactMilestoneJpaRepository.findById(artifactVersionId).map(this.mapper::mapToModel);
    }

    public ArtifactMilestone saveToDb(final ArtifactMilestone artifactMilestone) {
        final ArtifactMilestoneEntity savedVersion = this.artifactMilestoneJpaRepository.save(this.mapper.mapToEntity(artifactMilestone));
        return this.mapper.mapToModel(savedVersion);
    }

    public void deleteAllByArtifactId(final String artifactId) {
        final int deletedVersions = this.artifactMilestoneJpaRepository.deleteAllByArtifactId(artifactId);
        log.debug("Delete {} versions", deletedVersions);
    }

    public void deleteAllByRepositoryId(final String repositoryId) {
        final int deletedVersions = this.artifactMilestoneJpaRepository.deleteAllByRepositoryId(repositoryId);
        log.debug("Delete {} versions", deletedVersions);
    }

    public ByteArrayResource downloadMilestone(final String artifactVersionId) {
        log.debug("Querying version for download");
        final ArtifactMilestone artifactMilestone = this.getMilestone(artifactVersionId)
                .orElseThrow(() -> new ObjectNotFoundException("exception.versionNotFound"));
        return new ByteArrayResource(artifactMilestone.getFile().getBytes());
    }

    public List<ArtifactMilestone> getAllByDeploymentIds(final List<String> deploymentIds) {
        log.debug("Querying versions");
        return this.mapper.mapToModel(this.artifactMilestoneJpaRepository.findAllByDeployments_IdIn(deploymentIds));
    }
}
