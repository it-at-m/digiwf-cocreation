package io.miragon.bpmrepo.core.repository.domain.business;

import io.miragon.bpmrepo.core.repository.domain.mapper.RepositoryMapper;
import io.miragon.bpmrepo.core.repository.domain.model.NewRepository;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.model.RepositoryUpdate;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.RepositoryEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.RepoJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final RepositoryMapper mapper;
    private final RepoJpaRepository repoJpaRepository;

    public Repository createRepository(final NewRepository newRepository) {
        final Repository repository = new Repository(newRepository);
        return this.saveToDb(repository);
    }

    public void updateRepository(final String repositoryId, final RepositoryUpdate repositoryUpdate) {
        final Repository repository = this.getRepository(repositoryId);
        repository.update(repositoryUpdate);
        this.saveToDb(repository);
    }

    public Repository getRepository(final String repositoryId) {
        return this.repoJpaRepository.findById(repositoryId)
                .map(this.mapper::mapToModel)
                .orElseThrow();
    }

    public void updateAssignedUsers(final String repositoryId, final Integer assignedUsers) {
        final Repository repository = this.getRepository(repositoryId);
        repository.updateAssingedUsers(assignedUsers);
        this.repoJpaRepository.save(this.mapper.mapToEntity(repository));
    }

    public void updateExistingArtifacts(final String repositoryId, final Integer existingArtifacts) {
        final Repository repository = this.getRepository(repositoryId);
        repository.updateExistingArtifacts(existingArtifacts);
        this.repoJpaRepository.save(this.mapper.mapToEntity(repository));
    }

    public void deleteRepository(final String repositoryId) {
        this.repoJpaRepository.deleteById(repositoryId);
    }

    public Repository saveToDb(final Repository repository) {
        final RepositoryEntity savedRepository = this.repoJpaRepository.save(this.mapper.mapToEntity(repository));
        return this.mapper.mapToModel(savedRepository);
    }
}
