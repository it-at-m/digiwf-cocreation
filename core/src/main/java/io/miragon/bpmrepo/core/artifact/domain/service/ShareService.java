package io.miragon.bpmrepo.core.artifact.domain.service;

import io.miragon.bpmrepo.core.artifact.domain.mapper.SharedMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ShareWithRepository;
import io.miragon.bpmrepo.core.artifact.domain.model.ShareWithTeam;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ShareWithRepositoryEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ShareWithRepositoryId;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ShareWithTeamEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ShareWithTeamId;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.SharedRepositoryJpaRepository;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.SharedTeamJpaRepository;
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
    private final SharedRepositoryJpaRepository sharedRepositoryJpaRepository;
    private final SharedTeamJpaRepository sharedTeamJpaRepository;
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

    public ShareWithTeam shareWithTeam(final ShareWithTeam shareWithTeam) {
        log.debug("Persisting new share-relation with team");
        return this.saveShareWithTeam(shareWithTeam);
    }

    public ShareWithTeam updateShareWithTeam(final ShareWithTeam shareWithTeam) {
        log.debug("Persisting share-relation-update with team");
        final ShareWithTeam shared = this.getSharedWithTeamById(shareWithTeam.getArtifactId(), shareWithTeam.getTeamId());
        shared.setRole(shareWithTeam.getRole());
        return this.saveShareWithTeam(shared);
    }

    private ShareWithRepository saveShareWithRepository(final ShareWithRepository shareWithRepository) {
        final ShareWithRepositoryId shareWithRepositoryId = this.mapper.mapShareWithRepoIdToEmbeddable(shareWithRepository.getArtifactId(), shareWithRepository.getRepositoryId());
        final ShareWithRepositoryEntity shareWithRepositoryEntity = this.sharedRepositoryJpaRepository.save(this.mapper.mapShareWithRepositoryToEntity(shareWithRepository, shareWithRepositoryId));
        return this.mapper.mapShareWithRepositoryToModel(shareWithRepositoryEntity);

    }

    private ShareWithTeam saveShareWithTeam(final ShareWithTeam shareWithTeam) {
        final ShareWithTeamId shareWithTeamId = this.mapper.mapShareWithTeamIdToEmbeddable(shareWithTeam.getArtifactId(), shareWithTeam.getTeamId());
        final ShareWithTeamEntity shareWithTeamEntity = this.sharedTeamJpaRepository.save(this.mapper.mapShareWithTeamToEntity(shareWithTeam, shareWithTeamId));
        return this.mapper.mapShareWithTeamToModel(shareWithTeamEntity);
    }


    private ShareWithRepository getSharedWithRepoById(final String artifactId, final String repositoryId) {
        log.debug("Querying single repository-share-relation");
        return this.sharedRepositoryJpaRepository.findByShareWithRepositoryId_ArtifactIdAndShareWithRepositoryId_RepositoryId(artifactId, repositoryId).map(this.mapper::mapShareWithRepositoryToModel)
                .orElseThrow();
    }

    private ShareWithTeam getSharedWithTeamById(final String artifactId, final String teamId) {
        log.debug("Querying single team-share-relation");
        return this.sharedTeamJpaRepository.findByShareWithTeamId_ArtifactIdAndShareWithTeamId_TeamId(artifactId, teamId).map(this.mapper::mapShareWithTeamToModel)
                .orElseThrow();
    }

    public void deleteShareWithRepository(final String artifactId, final String repositoryId) {
        log.debug("Deleting share-relation");
        final int deletedRelations = this.sharedRepositoryJpaRepository.deleteByShareWithRepositoryId_ArtifactIdAndShareWithRepositoryId_RepositoryId(artifactId, repositoryId);
        if (deletedRelations != 1) {
            //TODO Throw custom error
            throw new RuntimeException();
        }
    }

    public void deleteShareWithTeam(final String artifactId, final String teamId) {
        log.debug("Deleting share-relation with team");
        final int deletedRelations = this.sharedTeamJpaRepository.deleteByShareWithTeamId_ArtifactIdAndShareWithTeamId_TeamId(artifactId, teamId);
        if (deletedRelations != 1) {
            //TODO Throw custom error
            throw new RuntimeException();
        }
    }

    public List<ShareWithRepository> getSharedArtifactsFromRepository(final String repositoryId) {
        log.debug("Querying Ids of artifacts shared with repository");
        return this.mapper.mapShareWithRepositoryToModel(this.sharedRepositoryJpaRepository.findByShareWithRepositoryId_RepositoryId(repositoryId));
    }

    public List<Artifact> getSharedArtifactsFromRepositories(final List<Repository> repositories) {
        log.debug("Querying all shared artifacts from List of Repositories");
        return repositories.stream()
                .flatMap(repository -> repository.getSharedArtifacts().stream())
                .collect(Collectors.toList());
    }

    public List<ShareWithRepository> getSharedRepositories(final String artifactId) {
        log.debug("Querying all repositories that can access the artifact");
        //returns: an Object containing ids, names werden in der Facade ergänzt
        return this.mapper.mapShareWithRepositoryToModel(this.sharedRepositoryJpaRepository.findByShareWithRepositoryId_ArtifactId(artifactId));
    }

    public List<ShareWithTeam> getSharedTeams(final String artifactId) {
        log.debug("Querying all repositories that can access the artifact");
        return this.mapper.mapShareWithTeamToModel(this.sharedTeamJpaRepository.findByShareWithTeamId_ArtifactId(artifactId));
    }

}
