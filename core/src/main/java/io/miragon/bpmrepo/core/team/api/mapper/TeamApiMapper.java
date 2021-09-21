package io.miragon.bpmrepo.core.team.api.mapper;

import io.miragon.bpmrepo.core.team.api.transport.NewTeamTO;
import io.miragon.bpmrepo.core.team.api.transport.TeamTO;
import io.miragon.bpmrepo.core.team.domain.model.NewTeam;
import io.miragon.bpmrepo.core.team.domain.model.Team;
import org.mapstruct.Mapper;

@Mapper
public interface TeamApiMapper {

    NewTeam mapToModel(final NewTeamTO to);


    NewTeamTO mapToTO(final NewTeam team);

    TeamTO mapToTO(final Team team);
}
