package io.miragon.bpmrepo.core.repository.domain.mapper;

import io.miragon.bpmrepo.core.repository.api.transport.AssignmentTO;
import io.miragon.bpmrepo.core.repository.domain.model.Assignment;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AssignmentMapper {

    Assignment toModel(AssignmentTO assignmentTO);

    @Mapping(source = "model", target = ".")
    @Mapping(source = "assignmentId", target = "assignmentId")
    AssignmentEntity toEntity(Assignment model, AssignmentId assignmentId);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "bpmnRepositoryId", target = "bpmnRepositoryId")
    AssignmentId toEmbeddable(String userId, String bpmnRepositoryId);

    @Mapping(target = "bpmnRepositoryId", expression = "java(assignmentEntity.getAssignmentId().getBpmnRepositoryId())")
    @Mapping(target = "userId", expression = "java(assignmentEntity.getAssignmentId().getUserId())")
    Assignment toModel(AssignmentEntity assignmentEntity);

    AssignmentTO toTO(Assignment assignment);
}
