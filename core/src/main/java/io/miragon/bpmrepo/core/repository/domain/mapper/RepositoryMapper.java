package io.miragon.bpmrepo.core.repository.domain.mapper;

import io.miragon.bpmrepo.core.repository.api.transport.BpmnRepositoryRequestTO;
import io.miragon.bpmrepo.core.repository.api.transport.BpmnRepositoryTO;
import io.miragon.bpmrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmrepo.core.repository.domain.model.BpmnRepository;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.BpmnRepositoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(imports = LocalDateTime.class)
public interface RepositoryMapper {
    BpmnRepositoryTO toTO(final BpmnRepositoryEntity entity);

    BpmnRepositoryRequestTO toRequestTO(final BpmnRepositoryEntity entity);

    @Mapping(target = "createdDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "assignedUsers", expression = "java(1)")
    @Mapping(target = "existingDiagrams", expression = "java(0)")
    BpmnRepository toModel(final NewBpmnRepositoryTO to);

    BpmnRepository toModel(final BpmnRepositoryEntity entity);

    BpmnRepositoryEntity toEntity(final BpmnRepository model);
}
