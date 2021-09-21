package io.miragon.bpmrepo.core.team.infrastructure.repository;

import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, String> {
    

}
