package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.service;

import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Artifact;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactUpdate;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.ArtifactEntity;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.repository.ArtifactJpaRepository;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.mapper.ArtifactMapper;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtifactService {

    private final ArtifactJpaRepository artifactJpaRepository;
    private final ArtifactMapper mapper;

    public Artifact createArtifact(final Artifact artifact) {
        log.debug("Persisting new Artifact {}", artifact);
        return this.saveArtifact(artifact);
    }

    public Artifact updateArtifact(final Artifact artifact, final ArtifactUpdate artifactUpdate) {
        log.debug("Persisting artifact update {}", artifactUpdate);
        artifact.updateArtifact(artifactUpdate);
        return this.saveArtifact(artifact);
    }

    public List<Artifact> getArtifactsByRepo(final String repositoryId) {
        log.debug("Querying artifacts in aepository");
        return this.mapper.mapToModel(this.artifactJpaRepository.findAllByRepositoryIdOrderByUpdatedDateDesc(repositoryId));
    }

    public Optional<Artifact> getArtifactById(final String artifactId) {
        log.debug("Querying single artifact");
        return this.artifactJpaRepository.findById(artifactId).map(this.mapper::mapToModel);
    }

    public List<Artifact> getAllArtifactsById(final List<String> artifactIds) {
        log.debug("Querying list of artifacts");
        return this.mapper.mapToModel(this.artifactJpaRepository.findAllByIdIn(artifactIds));
    }

    public List<Artifact> getAllArtifactsByIdAndType(final List<String> artifactIds, final String type) {
        log.debug("Querying list of artifacts");
        return this.mapper.mapToModel(this.artifactJpaRepository.findAllByIdInAndFileType(artifactIds, type));
    }

    public void updatedDate(final String artifactId) {
        final Artifact artifact = this.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        artifact.updateDate();
        this.saveArtifact(artifact);
    }

    private Artifact saveArtifact(final Artifact artifact) {
        final ArtifactEntity savedArtifact = this.artifactJpaRepository.save(this.mapper.mapToEntity(artifact));
        return this.mapper.mapToModel(savedArtifact);
    }

    public Integer countExistingArtifacts(final String repositoryId) {
        log.debug("Querying number of existing artifacts");
        return this.artifactJpaRepository.countAllByRepositoryId(repositoryId);
    }

    public void deleteArtifact(final String artifactId) {
        this.artifactJpaRepository.deleteById(artifactId);
        log.debug("Deleted artifact with ID {}", artifactId);
    }

    public void deleteAllByRepositoryId(final String repositoryId) {
        final int deletedArtifacts = this.artifactJpaRepository.deleteAllByRepositoryId(repositoryId);
        log.debug("Deleted {} artifacts", deletedArtifacts);
    }

    public List<Artifact> getRecent(final List<String> assignedRepositoryIds) {
        log.debug("Querying recent artifacts");
        return this.mapper.mapToModel(this.artifactJpaRepository.findTop20ByRepositoryIdInOrderByUpdatedDateDesc(assignedRepositoryIds));
    }

    public List<Artifact> searchArtifacts(final List<String> assignedRepoIds, final String typedTitle) {
        log.debug("Querying artifacts that match the search string");
        return this.mapper.mapToModel(this.artifactJpaRepository.findAllByRepositoryIdInAndNameLikeIgnoreCase(assignedRepoIds, "%" + typedTitle + "%"));
    }

    public Artifact lockArtifact(final String artifactId, final String username) {
        log.debug("Persisting artifact-lock for artifact {} for user {}", artifactId, username);
        final Artifact artifact = this.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        artifact.lock(username);
        return this.saveArtifact(artifact);
    }

    public Artifact unlockArtifact(final String artifactId) {
        log.debug("Releasing artifact-lock for artifact {}", artifactId);
        final Artifact artifact = this.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        artifact.unlock();
        return ArtifactService.this.saveArtifact(artifact);
    }

    public List<Artifact> getByRepoIdAndType(final String repositoryId, final String type) {
        log.debug("Querying artifacts of type {} from repository {}", type, repositoryId);
        return this.mapper.mapToModel(this.artifactJpaRepository.findAllByRepositoryIdAndFileTypeIgnoreCase(repositoryId, type));
    }
}
