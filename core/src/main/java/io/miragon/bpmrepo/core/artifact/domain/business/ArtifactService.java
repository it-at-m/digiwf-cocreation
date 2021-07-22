package io.miragon.bpmrepo.core.artifact.domain.business;

import io.miragon.bpmrepo.core.artifact.domain.mapper.ArtifactMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactUpdate;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.ArtifactJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtifactService {

    private final ArtifactJpaRepository artifactJpaRepository;
    private final ArtifactMapper mapper;

    public Artifact createArtifact(final Artifact artifact) {
        return this.saveArtifact(artifact);
    }

    public Artifact updateArtifact(final String artifactId, final ArtifactUpdate artifactUpdate) {
        log.debug("Updating Daigram " + artifactUpdate);
        final Artifact artifact = this.getArtifactsById(artifactId);
        artifact.updateArtifact(artifactUpdate);
        return this.saveArtifact(artifact);
    }

    public List<Artifact> getArtifactsByRepo(final String repositoryId) {
        final List<ArtifactEntity> artifacts = this.artifactJpaRepository.findAllByRepositoryId(repositoryId);
        return this.mapper.mapToModel(artifacts);
    }

    public Artifact getArtifactsById(final String artifactId) {
        return this.artifactJpaRepository.findById(artifactId)
                .map(this.mapper::mapToModel)
                .orElseThrow();
    }

    public void updateUpdatedDate(final String artifactId) {
        final Artifact artifact = this.getArtifactsById(artifactId);
        artifact.updateDate();
        this.saveArtifact(artifact);
    }

    private Artifact saveArtifact(final Artifact bpmnArtifact) {
        val savedArtifact = this.artifactJpaRepository.save(this.mapper.mapToEntity(bpmnArtifact));
        return this.mapper.mapToModel(savedArtifact);
    }

    public Integer countExistingArtifacts(final String repositoryId) {
        return this.artifactJpaRepository.countAllByRepositoryId(repositoryId);
    }

    public void deleteArtifact(final String artifactId) {
        this.artifactJpaRepository.deleteById(artifactId);
        log.info(String.format("Deleted %s Artifact", artifactId));
    }

    public void deleteAllByRepositoryId(final String bpmnRepositoryId) {
        final int deletedArtifacts = this.artifactJpaRepository.deleteAllByRepositoryId(bpmnRepositoryId);
        log.debug(String.format("Deleted %s artifacts", deletedArtifacts));
    }

    public List<Artifact> getRecent(final List<String> assignments) {
        //TODO Improve performance -> save in separate db
        final List<ArtifactEntity> artifacts = assignments.stream()
                .map(this.artifactJpaRepository::findAllByRepositoryId)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(a -> Timestamp.valueOf(a.getUpdatedDate())))
                .collect(Collectors.toList());
        Collections.reverse(artifacts);
        return this.mapper.mapToModel(artifacts.subList(0, Math.min(artifacts.size(), 10)));
    }

    public void updatePreviewSVG(final String artifactId, final String svgPreview) {
        final Artifact artifact = this.getArtifactsById(artifactId);
        artifact.updateSvgPreview(svgPreview);
        this.saveArtifact(artifact);
    }

    public List<Artifact> searchArtifacts(final List<String> assignedRepoIds, final String typedTitle) {
        final List<ArtifactEntity> assignedArtifacts = this.artifactJpaRepository.findAllByRepositoryIdInAndNameStartsWithIgnoreCase(assignedRepoIds, typedTitle);
        return this.mapper.mapToModel(assignedArtifacts);
    }

    public void lockArtifact(final String artifactId, final String username) {
        final Artifact artifact = this.getArtifactsById(artifactId);
        artifact.lock(username);
        this.saveArtifact(artifact);
    }

    public void unlockArtifact(final String artifactId) {
        final Artifact artifact = this.getArtifactsById(artifactId);
        artifact.unlock();
        ArtifactService.this.saveArtifact(artifact);
    }
}
