package io.miragon.bpmrepo.core.team.domain.mapper;

import io.miragon.bpmrepo.core.team.domain.model.RepoTeamAssignment;
import io.miragon.bpmrepo.core.team.infrastructure.entity.RepoTeamAssignmentEntity;
import io.miragon.bpmrepo.core.team.infrastructure.entity.RepoTeamAssignmentId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface RepoTeamAssignmentMapper {

    @Mapping(source = "repositoryId", target = "repositoryId")
    @Mapping(source = "teamId", target = "teamId")
    RepoTeamAssignmentId mapToEmbeddable(String repositoryId, String teamId);

    @Mapping(source = "model", target = ".")
    @Mapping(source = "repoTeamAssignmentId", target = "repoTeamAssignmentId")
    RepoTeamAssignmentEntity mapToEntity(RepoTeamAssignment model, RepoTeamAssignmentId repoTeamAssignmentId);

    @Mapping(target = "teamId", expression = "java(entity.getRepoTeamAssignmentId().getTeamId())")
    @Mapping(target = "repositoryId", expression = "java(entity.getRepoTeamAssignmentId().getRepositoryId())")
    RepoTeamAssignment mapToModel(RepoTeamAssignmentEntity entity);

    @Mapping(target = "teamId", expression = "java(entity.getRepoTeamAssignmentId().getTeamId())")
    @Mapping(target = "repositoryId", expression = "java(entity.getRepoTeamAssignmentId().getRepositoryId())")
    List<RepoTeamAssignment> mapToModel(List<RepoTeamAssignmentEntity> entities);
}
