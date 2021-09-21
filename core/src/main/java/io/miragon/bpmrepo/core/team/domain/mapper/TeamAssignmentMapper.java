package io.miragon.bpmrepo.core.team.domain.mapper;

import io.miragon.bpmrepo.core.team.api.transport.TeamAssignmentTO;
import io.miragon.bpmrepo.core.team.domain.model.TeamAssignment;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamAssignmentEntity;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamAssignmentId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TeamAssignmentMapper {

    TeamAssignment mapToModel(TeamAssignmentTO to);

    TeamAssignmentTO mapToTO(TeamAssignment model);

    TeamAssignmentEntity mapToEntity(TeamAssignment model);

    TeamAssignment mapToModel(TeamAssignmentEntity entity);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "teamId", target = "teamId")
    TeamAssignmentId mapToEmbeddable(String userId, String teamId);

    @Mapping(source = "model", target = ".")
    @Mapping(source = "teamAssignmentId", target = "teamAssignmentId")
    TeamAssignmentEntity mapToEntity(TeamAssignment model, TeamAssignmentId teamAssignmentId);
}
