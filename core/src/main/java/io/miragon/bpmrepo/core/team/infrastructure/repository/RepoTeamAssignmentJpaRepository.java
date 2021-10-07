package io.miragon.bpmrepo.core.team.infrastructure.repository;


import io.miragon.bpmrepo.core.team.infrastructure.entity.RepoTeamAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoTeamAssignmentJpaRepository extends JpaRepository<RepoTeamAssignmentEntity, String> {

    Optional<RepoTeamAssignmentEntity> findByRepoTeamAssignmentId_RepositoryIdAndRepoTeamAssignmentId_TeamId(String repositoryId, String teamId);

    List<RepoTeamAssignmentEntity> findAllByRepoTeamAssignmentId_RepositoryId(String repositoryId);

    List<RepoTeamAssignmentEntity> findAllByRepoTeamAssignmentId_TeamId(String teamId);

}
