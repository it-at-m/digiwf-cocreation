package io.miragon.bpmrepo.core.team.infrastructure.repository;

import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamAssignmentJpaRepository extends JpaRepository<TeamAssignmentEntity, String> {

    Optional<TeamAssignmentEntity> findByTeamAssignmentId_TeamIdAndTeamAssignmentId_UserId(String teamId, String userId);

    Integer countByTeamAssignmentId_TeamId(String teamId);

    List<TeamAssignmentEntity> findAllByTeamAssignmentId_UserId(String userId);

    List<TeamAssignmentEntity> findAllByTeamAssignmentId_TeamId(String teamId);

    int deleteByTeamAssignmentId_TeamIdAndTeamAssignmentId_UserId(String teamId, String userId);


}
