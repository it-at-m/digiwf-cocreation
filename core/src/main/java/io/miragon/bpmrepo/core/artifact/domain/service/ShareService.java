package io.miragon.bpmrepo.core.artifact.domain.service;

import io.miragon.bpmrepo.core.artifact.api.transport.ShareWithRepositoryTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ShareWithTeamTO;
import io.miragon.bpmrepo.core.artifact.domain.mapper.SharedMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.Shared;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.SharedEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.SharedId;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.SharedJpaRepository;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShareService {
    private final SharedJpaRepository sharedJpaRepository;
    private final SharedMapper mapper;

    public Shared shareWithRepository(final ShareWithRepositoryTO shareWithRepositoryTO) {
        log.debug("Persisting new share-relation with repository");
        final Shared shared = new Shared(shareWithRepositoryTO);
        return this.saveShare(shared);
    }

    public Shared updateShareWithRepository(final ShareWithRepositoryTO shareWithRepositoryTO) {
        log.debug("Persisting share-relation-update with repository");
        final Shared shared = this.getSharedWithRepoById(shareWithRepositoryTO.getArtifactId(), shareWithRepositoryTO.getRepositoryId());
        shared.updateRole(shareWithRepositoryTO.getRole());
        return this.saveShare(shared);
    }

    public Shared shareWithTeam(final ShareWithTeamTO shareWithTeamTO) {
        log.debug("Persisting new share-relation with team");
        final Shared shared = new Shared(shareWithTeamTO);
        return this.saveShare(shared);
    }

    public Shared updateShareWithTeam(final ShareWithTeamTO shareWithTeamTO) {
        log.debug("Persisting share-relation-update with team");
        final Shared shared = this.getSharedWithRepoById(shareWithTeamTO.getArtifactId(), shareWithTeamTO.getTeamId());
        shared.updateRole(shareWithTeamTO.getRole());
        return this.saveShare(shared);
    }

    private Shared saveShare(final Shared shared) {
        final SharedId sharedId = this.mapper.mapToEmbeddable(shared.getArtifactId(), shared.getRepositoryId(), shared.getTeamId());
        final SharedEntity sharedEntity = this.sharedJpaRepository.save(this.mapper.mapToEntity(shared, sharedId));
        return this.mapper.mapToModel(sharedEntity);
    }


    private Shared getSharedWithRepoById(final String artifactId, final String repositoryId) {
        log.debug("Querying single share-relation");
        return this.sharedJpaRepository.findBySharedId_ArtifactIdAndSharedId_RepositoryId(artifactId, repositoryId).map(this.mapper::mapToModel)
                .orElseThrow();
    }

    public void deleteShareWithRepository(final String artifactId, final String repositoryId) {
        log.debug("Deleting share-relation");
        final int deletedRelations = this.sharedJpaRepository.deleteBySharedId_ArtifactIdAndSharedId_RepositoryId(artifactId, repositoryId);
        if (deletedRelations != 1) {
            //TODO Throw custom error
            throw new RuntimeException();
        }
    }

    public void deleteShareWithTeam(final String artifactId, final String teamId) {
        log.debug("Deleting share-relation with team");
        final int deletedRelations = this.sharedJpaRepository.deleteBySharedId_ArtifactIdAndSharedId_TeamId(artifactId, teamId);
        if (deletedRelations != 1) {
            //TODO Throw custom error
            throw new RuntimeException();
        }
    }

    public List<Shared> getSharedArtifactsFromRepository(final String repositoryId) {
        log.debug("Querying Ids of artifacts shared with repository");
        return this.sharedJpaRepository.findBySharedId_RepositoryId(repositoryId).map(this.mapper::mapToModel)
                .orElseThrow();
    }


    public List<Artifact> getSharedArtifactsFromRepositories(final List<Repository> repositories) {
        log.debug("Querying all shared artifacts from List of Repositories");
        return repositories.stream()
                .flatMap(repository -> repository.getSharedArtifacts().stream())
                .collect(Collectors.toList());
    }

    public List<Shared> getSharedRepositories(final String artifactId) {
        log.debug("Querying all repositories that can access the artifact");
        return this.sharedJpaRepository.findBySharedId_ArtifactIdAndSharedId_RepositoryIdNotNull(artifactId).map(this.mapper::mapToModel)
                .orElseThrow();
    }

}
