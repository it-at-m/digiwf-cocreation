package de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.repository;

import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.StarredEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StarredJpaRepository extends JpaRepository<StarredEntity, String> {

    int deleteById_artifactIdAndId_UserId(String artifactId, String userId);

    int deleteAllById_artifactId(String artifactId);

    int deleteAllById_artifactIdIn(List<String> artifactIds);

    Optional<StarredEntity> findById_artifactIdAndId_UserId(String artifactId, String userId);

    List<StarredEntity> findAllById_UserId(String userId);

}
