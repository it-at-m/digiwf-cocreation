package de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.repository;

import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoJpaRepository extends JpaRepository<RepositoryEntity, String> {
    List<RepositoryEntity> findAllByNameIsNot(String name);

    List<RepositoryEntity> findAllByIdIn(List<String> repositoryIds);

    RepositoryEntity findByIdAndName(String repositoryId, String repositoryName);

    List<RepositoryEntity> findAllByNameStartsWithIgnoreCase(String typedName);
}
