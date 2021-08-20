package io.miragon.bpmrepo.core.repository.infrastructure.repository;

import io.miragon.bpmrepo.core.repository.infrastructure.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoJpaRepository extends JpaRepository<RepositoryEntity, String> {
    List<RepositoryEntity> findAllByNameIsNot(String name);

    Optional<List<RepositoryEntity>> findAllByIdIn(List<String> repositoryIds);

    RepositoryEntity findByIdAndName(String repositoryId, String repositoryName);

    List<RepositoryEntity> findAllByNameStartsWithIgnoreCase(String typedName);
}
