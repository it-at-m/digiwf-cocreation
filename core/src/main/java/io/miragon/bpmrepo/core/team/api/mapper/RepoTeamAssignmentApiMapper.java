package io.miragon.bpmrepo.core.team.api.mapper;

import io.miragon.bpmrepo.core.team.api.transport.RepoTeamAssignmentTO;
import io.miragon.bpmrepo.core.team.domain.model.RepoTeamAssignment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RepoTeamAssignmentApiMapper {

    RepoTeamAssignmentTO mapToTO(RepoTeamAssignment model);

    List<RepoTeamAssignmentTO> mapToTO(List<RepoTeamAssignment> model);

    RepoTeamAssignment mapToModel(RepoTeamAssignmentTO to);
}
