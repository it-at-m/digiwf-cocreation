package io.miragon.bpmrepo.core.team.infrastructure.repository;

import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, String> {

    List<TeamEntity> findAllByNameStartsWithIgnoreCase(String typedName);

}
