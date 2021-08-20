package io.miragon.bpmrepo.core.repository.domain.service;

import io.miragon.bpmrepo.core.repository.domain.mapper.RepositoryMapper;
import io.miragon.bpmrepo.core.repository.domain.model.NewRepository;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.model.RepositoryUpdate;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.RepositoryEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.RepoJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final RepositoryMapper mapper;
    private final RepoJpaRepository repoJpaRepository;

    public Repository createRepository(final NewRepository newRepository) {
        log.debug("Persisting new repository");
        final Repository repository = new Repository(newRepository);
        return this.saveToDb(repository);
    }

    public Repository updateRepository(final String repositoryId, final RepositoryUpdate repositoryUpdate) {
        log.debug("Persisting updates");
        final Repository repository = this.getRepository(repositoryId);
        repository.update(repositoryUpdate);
        return this.saveToDb(repository);
    }

    public Repository getRepository(final String repositoryId) {
        log.debug("Querying repository");
        return this.repoJpaRepository.findById(repositoryId)
                .map(this.mapper::mapToModel)
                .orElseThrow();
    }

    public List<Repository> getRepositories(final List<String> repositoryIds) {
        log.debug("Querying repositories");
        return this.repoJpaRepository.findAllByIdIn(repositoryIds)
                .map(this.mapper::mapToModel)
                .orElseThrow();
    }


    public void updateAssignedUsers(final String repositoryId, final Integer assignedUsers) {
        final Repository repository = this.getRepository(repositoryId);
        repository.updateAssingedUsers(assignedUsers);
        this.repoJpaRepository.save(this.mapper.mapToEntity(repository));
    }

    public void updateExistingArtifacts(final String repositoryId, final Integer existingArtifacts) {
        log.debug("Persisting new number of artifacts in repository");
        final Repository repository = this.getRepository(repositoryId);
        repository.updateExistingArtifacts(existingArtifacts);
        this.repoJpaRepository.save(this.mapper.mapToEntity(repository));
    }

    public void deleteRepository(final String repositoryId) {
        log.debug("Deleting repository");
        this.repoJpaRepository.deleteById(repositoryId);
    }

    public List<Repository> searchRepositories(final String typedName) {
        log.debug("Querying repositories that match the search string");
        return this.mapper.mapToModel(this.repoJpaRepository.findAllByNameStartsWithIgnoreCase(typedName));
    }

    public Repository saveToDb(final Repository repository) {
        final RepositoryEntity savedRepository = this.repoJpaRepository.save(this.mapper.mapToEntity(repository));
        return this.mapper.mapToModel(savedRepository);
    }
}
