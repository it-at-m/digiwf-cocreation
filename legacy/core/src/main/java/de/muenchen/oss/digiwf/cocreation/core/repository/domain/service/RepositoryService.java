package de.muenchen.oss.digiwf.cocreation.core.repository.domain.service;

import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.RepositoryEntity;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.repository.RepoJpaRepository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.mapper.RepositoryMapper;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.NewRepository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.Repository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.RepositoryUpdate;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        final Repository repository = this.getRepository(repositoryId).orElseThrow(() -> new ObjectNotFoundException("exception.repositoryNotFound"));
        repository.update(repositoryUpdate);
        return this.saveToDb(repository);
    }

    public Optional<Repository> getRepository(final String repositoryId) {
        log.debug("Querying repository");
        return this.repoJpaRepository.findById(repositoryId).map(this.mapper::mapToModel);
    }

    public List<Repository> getRepositories(final List<String> repositoryIds) {
        log.debug("Querying repositories");
        return this.mapper.mapToModel(this.repoJpaRepository.findAllByIdIn(repositoryIds));
    }

    public void updateAssignedUsers(final String repositoryId, final Integer assignedUsers) {
        final Repository repository = this.getRepository(repositoryId).orElseThrow(() -> new ObjectNotFoundException("exception.repositoryNotFound"));
        repository.updateAssingedUsers(assignedUsers);
        this.repoJpaRepository.save(this.mapper.mapToEntity(repository));
    }

    public void updateExistingArtifacts(final String repositoryId, final Integer existingArtifacts) {
        log.debug("Persisting new number of artifacts in repository");
        final Repository repository = this.getRepository(repositoryId).orElseThrow(() -> new ObjectNotFoundException("exception.repositoryNotFound"));
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

    //------------------------------ HELPER METHODS ------------------------------//

    private Repository saveToDb(final Repository repository) {
        final RepositoryEntity savedRepository = this.repoJpaRepository.save(this.mapper.mapToEntity(repository));
        return this.mapper.mapToModel(savedRepository);
    }


}
