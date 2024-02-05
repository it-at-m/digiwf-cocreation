package de.muenchen.oss.digiwf.cocreation.core.sharing.infrastructure.repository;

import de.muenchen.oss.digiwf.cocreation.core.sharing.infrastructure.entity.ShareWithRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharedRepositoryJpaRepository extends JpaRepository<ShareWithRepositoryEntity, String> {
    Optional<ShareWithRepositoryEntity> findByShareWithRepositoryId_ArtifactIdAndShareWithRepositoryId_RepositoryId(final String artifactId, final String repositoryId);

    int deleteByShareWithRepositoryId_ArtifactIdAndShareWithRepositoryId_RepositoryId(final String artifactId, final String repositoryId);


    List<ShareWithRepositoryEntity> findByShareWithRepositoryId_ArtifactId(final String artifactId);

    List<ShareWithRepositoryEntity> findByShareWithRepositoryId_RepositoryId(final String repositoryId);

    List<ShareWithRepositoryEntity> findAllByShareWithRepositoryId_RepositoryIdIn(final List<String> repositoryIds);
}
