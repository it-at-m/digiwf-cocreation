package io.miragon.bpmrepo.core.team.domain.mapper;

import io.miragon.bpmrepo.core.team.domain.model.Team;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TeamMapper {

    Team mapToModel(final TeamEntity entity);

    List<Team> mapToModel(final List<TeamEntity> entities);

    TeamEntity mapToEntity(final Team model);

}
