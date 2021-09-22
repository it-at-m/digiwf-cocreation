package io.miragon.bpmrepo.core.team.domain.mapper;

import io.miragon.bpmrepo.core.team.api.transport.TeamAssignmentTO;
import io.miragon.bpmrepo.core.team.domain.model.TeamAssignment;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamAssignmentEntity;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamAssignmentId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface TeamAssignmentMapper {

    TeamAssignment mapToModel(TeamAssignmentTO to);

    TeamAssignmentTO mapToTO(TeamAssignment model);

    TeamAssignmentEntity mapToEntity(TeamAssignment model);

    @Mapping(target = "teamId", expression = "java(entity.getTeamAssignmentId().getTeamId())")
    @Mapping(target = "userId", expression = "java(entity.getTeamAssignmentId().getUserId())")
    TeamAssignment mapToModel(TeamAssignmentEntity entity);

    List<TeamAssignment> mapToModel(List<TeamAssignmentEntity> entities);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "teamId", target = "teamId")
    TeamAssignmentId mapToEmbeddable(String userId, String teamId);

    @Mapping(source = "model", target = ".")
    @Mapping(source = "teamAssignmentId", target = "teamAssignmentId")
    TeamAssignmentEntity mapToEntity(TeamAssignment model, TeamAssignmentId teamAssignmentId);
}
