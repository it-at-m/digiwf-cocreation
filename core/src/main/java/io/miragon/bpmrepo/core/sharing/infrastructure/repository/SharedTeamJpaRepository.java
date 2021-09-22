package io.miragon.bpmrepo.core.sharing.infrastructure.repository;

import io.miragon.bpmrepo.core.sharing.infrastructure.entity.ShareWithTeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharedTeamJpaRepository extends JpaRepository<ShareWithTeamEntity, String> {
    Optional<ShareWithTeamEntity> findByShareWithTeamId_ArtifactIdAndShareWithTeamId_TeamId(final String artifacId, final String teamId);

    int deleteByShareWithTeamId_ArtifactIdAndShareWithTeamId_TeamId(final String artifactId, final String teamId);

    List<ShareWithTeamEntity> findByShareWithTeamId_ArtifactId(final String artifactId);

    List<ShareWithTeamEntity> findByShareWithTeamId_TeamId(final String teamId);

}
