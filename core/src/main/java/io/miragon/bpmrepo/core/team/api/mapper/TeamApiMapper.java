package io.miragon.bpmrepo.core.team.api.mapper;

import io.miragon.bpmrepo.core.team.api.transport.NewTeamTO;
import io.miragon.bpmrepo.core.team.api.transport.TeamTO;
import io.miragon.bpmrepo.core.team.api.transport.TeamUpdateTO;
import io.miragon.bpmrepo.core.team.domain.model.NewTeam;
import io.miragon.bpmrepo.core.team.domain.model.Team;
import io.miragon.bpmrepo.core.team.domain.model.TeamUpdate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TeamApiMapper {

    NewTeam mapToModel(final NewTeamTO to);

    NewTeamTO mapToTO(final NewTeam team);

    Team mapToModel(final TeamTO to);

    TeamTO mapToTO(final Team team);

    TeamUpdate mapToModel(final TeamUpdateTO to);

    List<TeamTO> mapToTO(final List<Team> team);
}
