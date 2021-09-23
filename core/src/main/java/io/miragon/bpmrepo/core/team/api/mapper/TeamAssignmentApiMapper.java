package io.miragon.bpmrepo.core.team.api.mapper;

import io.miragon.bpmrepo.core.team.api.transport.TeamAssignmentTO;
import io.miragon.bpmrepo.core.team.domain.model.TeamAssignment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TeamAssignmentApiMapper {

    TeamAssignmentTO mapToTO(TeamAssignment model);


    List<TeamAssignmentTO> mapToTO(List<TeamAssignment> model);
}
