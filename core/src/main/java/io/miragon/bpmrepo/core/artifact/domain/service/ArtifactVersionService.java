package io.miragon.bpmrepo.core.artifact.domain.service;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.mapper.VersionMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersionUpdate;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactVersionEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.ArtifactVersionJpaRepository;
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
public class ArtifactVersionService {

    private final ArtifactVersionJpaRepository artifactVersionJpaRepository;
    private final VersionMapper mapper;

    public ArtifactVersion updateVersion(final ArtifactVersionUpdate artifactVersionUpdate) {
        log.debug("Persisting version-update");
        final Optional<ArtifactVersion> updatedVersionOpt = this.getVersion(artifactVersionUpdate.getVersionId());
        if (updatedVersionOpt.isEmpty()) {
            throw new ObjectNotFoundException("exception.versionNotFound");
        }
        final ArtifactVersion updatedVersion = updatedVersionOpt.get();
        updatedVersion.updateVersion(artifactVersionUpdate);
        return this.saveToDb(updatedVersion);
    }

    public void setVersionOutdated(final ArtifactVersion artifactVersion) {
        artifactVersion.setOutdated();
        this.saveToDb(artifactVersion);
    }


    public ArtifactVersion createNewVersion(final ArtifactVersion artifactVersion) {
        log.debug("Persisting new version");
        final ArtifactVersion latestVersion = this.getLatestVersion(artifactVersion.getArtifactId());
        artifactVersion.updateMilestone(latestVersion.getMilestone() + 1);
        return this.saveToDb(artifactVersion);
    }

    public ArtifactVersion createInitialVersion(final ArtifactVersion artifactVersion) {
        log.debug("Persisting initial version");
        artifactVersion.updateMilestone(1);
        return this.saveToDb(artifactVersion);
    }

    public List<ArtifactVersion> getAllVersions(final String artifactId) {
        log.debug("Querying all versions");
        final List<ArtifactVersionEntity> artifactVersionEntities = this.artifactVersionJpaRepository.findAllByArtifactId(artifactId);
        return this.mapper.mapToModel(artifactVersionEntities);
    }

    public ArtifactVersion getLatestVersion(final String artifactId) {
        log.debug("Querying latest version");
        return this.artifactVersionJpaRepository
                .findFirstByArtifactIdOrderByMilestoneDesc(artifactId)
                .map(this.mapper::mapToModel)
                .orElseThrow();
    }

    public Optional<ArtifactVersion> getVersion(final String artifactVersionId) {
        log.debug("Querying specific version");
        return this.artifactVersionJpaRepository.findById(artifactVersionId).map(this.mapper::mapToModel);
    }

    public ArtifactVersion saveToDb(final ArtifactVersion artifactVersion) {
        final ArtifactVersionEntity savedVersion = this.artifactVersionJpaRepository.save(this.mapper.mapToEntity(artifactVersion));
        return (this.mapper.mapToModel(savedVersion));
    }

    public void deleteAllByArtifactId(final String artifactId) {
        final int deletedVersions = this.artifactVersionJpaRepository.deleteAllByArtifactId(artifactId);
        log.debug("Deleted {} versions", deletedVersions);
    }

    public void deleteAllByRepositoryId(final String repositoryId) {
        final int deletedVersions = this.artifactVersionJpaRepository.deleteAllByRepositoryId(repositoryId);
        log.debug("Deleted {} versions", deletedVersions);
    }

    public void deleteAutosavedVersions(final String repositoryId, final String artifactId) {
        log.debug("Deleting all versions saved by Autosave");
        this.artifactVersionJpaRepository.deleteAllByRepositoryIdAndArtifactIdAndSaveType(repositoryId, artifactId, SaveTypeEnum.AUTOSAVE);
    }

    public ByteArrayResource downloadVersion(final String artifactVersionId) {
        log.debug("Querying version for download");
        final Optional<ArtifactVersion> artifactVersionOpt = this.getVersion(artifactVersionId);
        if (artifactVersionOpt.isEmpty()) {
            throw new ObjectNotFoundException("exception.versionNotFound");
        }
        final ArtifactVersion artifactVersion = artifactVersionOpt.get();
        final ByteArrayResource resource = new ByteArrayResource(artifactVersion.getFile().getBytes());
        return resource;
    }
}
