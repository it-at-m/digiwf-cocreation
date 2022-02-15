package io.miragon.bpmrepo.core.sharing.domain.service;

import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import io.miragon.bpmrepo.core.sharing.domain.mapper.SharedMapper;
import io.miragon.bpmrepo.core.sharing.domain.model.ShareWithRepository;
import io.miragon.bpmrepo.core.sharing.infrastructure.entity.ShareWithRepositoryEntity;
import io.miragon.bpmrepo.core.sharing.infrastructure.entity.ShareWithRepositoryId;
import io.miragon.bpmrepo.core.sharing.infrastructure.repository.SharedRepositoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShareService {
    private final SharedRepositoryJpaRepository sharedRepositoryJpaRepository;
    private final SharedMapper mapper;

    public ShareWithRepository shareWithRepository(final ShareWithRepository shareWithRepository) {
        log.debug("Persisting new share-relation with repository");
        return this.saveShareWithRepository(shareWithRepository);
    }

    public ShareWithRepository updateShareWithRepository(final ShareWithRepository shareWithRepository) {
        log.debug("Persisting share-relation-update with repository");
        final ShareWithRepository shared = this.getSharedWithRepoById(shareWithRepository.getArtifactId(), shareWithRepository.getRepositoryId());
        shared.setRole(shareWithRepository.getRole());
        return this.saveShareWithRepository(shared);
    }

    private ShareWithRepository saveShareWithRepository(final ShareWithRepository shareWithRepository) {
        final ShareWithRepositoryId shareWithRepositoryId = this.mapper.mapShareWithRepoIdToEmbeddable(shareWithRepository.getArtifactId(), shareWithRepository.getRepositoryId());
        final ShareWithRepositoryEntity shareWithRepositoryEntity = this.sharedRepositoryJpaRepository.save(this.mapper.mapShareWithRepositoryToEntity(shareWithRepository, shareWithRepositoryId));
        return this.mapper.mapShareWithRepositoryToModel(shareWithRepositoryEntity);
    }

    private ShareWithRepository getSharedWithRepoById(final String artifactId, final String repositoryId) {
        log.debug("Querying single repository-share-relation");
        return this.sharedRepositoryJpaRepository.findByShareWithRepositoryId_ArtifactIdAndShareWithRepositoryId_RepositoryId(artifactId, repositoryId).map(this.mapper::mapShareWithRepositoryToModel)
                .orElseThrow(() -> new ObjectNotFoundException("exception.shareWithRepositoryNotFound"));
    }

    public void deleteShareWithRepository(final String artifactId, final String repositoryId) {
        log.debug("Deleting share-relation");
        final int deletedRelations = this.sharedRepositoryJpaRepository.deleteByShareWithRepositoryId_ArtifactIdAndShareWithRepositoryId_RepositoryId(artifactId, repositoryId);
        if (deletedRelations != 1) {
            //TODO Throw custom error
            throw new RuntimeException();
        }
    }

    public List<ShareWithRepository> getSharedArtifactsFromRepository(final String repositoryId) {
        log.debug("Querying Ids of artifacts shared with repository");
        return this.mapper.mapShareWithRepositoryToModel(this.sharedRepositoryJpaRepository.findByShareWithRepositoryId_RepositoryId(repositoryId));
    }

    public List<String> getSharedArtifactIdsFromRepositories(final List<String> repositoryIds) {
        log.debug("Querying all shared artifacts from List of Repositories");
        final List<ShareWithRepositoryEntity> shareWithRepositoryEntities = this.sharedRepositoryJpaRepository.findAllByShareWithRepositoryId_RepositoryIdIn(repositoryIds);
        return shareWithRepositoryEntities.stream().map(shareWithRepositoryEntity -> shareWithRepositoryEntity.getShareWithRepositoryId().getArtifactId()).collect(Collectors.toList());
    }

    public List<ShareWithRepository> getSharedRepositories(final String artifactId) {
        log.debug("Querying all repositories that can access the artifact");
        //returns: an Object containing ids, names will be added in the facade
        return this.mapper.mapShareWithRepositoryToModel(this.sharedRepositoryJpaRepository.findByShareWithRepositoryId_ArtifactId(artifactId));
    }

}
