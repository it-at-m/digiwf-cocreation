package io.miragon.bpmrepo.core.diagram.domain.mapper;

import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.DiagramEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(imports = LocalDateTime.class)
public interface DiagramMapper {

    Diagram mapToModel(final DiagramEntity entity);

    List<Diagram> mapToModel(final List<DiagramEntity> list);

    @Mapping(target = "createdDate", expression = "java((model.getCreatedDate() == null) ? LocalDateTime.now() : model.getCreatedDate())")
    @Mapping(target = "updatedDate", expression = "java(LocalDateTime.now())")
    DiagramEntity mapToEntity(final Diagram model);
}
