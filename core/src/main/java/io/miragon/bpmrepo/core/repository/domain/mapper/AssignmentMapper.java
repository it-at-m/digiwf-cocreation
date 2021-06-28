package io.miragon.bpmrepo.core.repository.domain.mapper;

import io.miragon.bpmrepo.core.repository.domain.model.Assignment;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AssignmentMapper {

    @Mapping(source = "model", target = ".")
    @Mapping(source = "assignmentId", target = "assignmentId")
    AssignmentEntity mapToEntity(Assignment model, AssignmentId assignmentId);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "repositoryId", target = "repositoryId")
    AssignmentId mapToEmbeddable(String userId, String repositoryId);

    @Mapping(target = "repositoryId", expression = "java(assignmentEntity.getAssignmentId().getRepositoryId())")
    @Mapping(target = "userId", expression = "java(assignmentEntity.getAssignmentId().getUserId())")
    Assignment mapToModel(AssignmentEntity assignmentEntity);

    List<Assignment> mapToModel(List<AssignmentEntity> list);

}
