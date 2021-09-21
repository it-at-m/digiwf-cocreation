package io.miragon.bpmrepo.core.team.domain.mapper;

import io.miragon.bpmrepo.core.team.domain.model.Team;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamEntity;
import org.mapstruct.Mapper;

@Mapper
public interface TeamMapper {

    Team mapToModel(final TeamEntity entity);

    TeamEntity mapToEntity(final Team model);

}
