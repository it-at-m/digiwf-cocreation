package io.miragon.bpmnrepo.core.diagram.domain.mapper;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagram;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = LocalDateTime.class)
public interface DiagramMapper {

    BpmnDiagram toModel(final BpmnDiagramTO to);

    BpmnDiagram toModel(final BpmnDiagramEntity entity);

    @Mapping(target = "createdDate", expression="java((model.getCreatedDate() == null) ? LocalDateTime.now() : model.getCreatedDate())")
    @Mapping(target = "updatedDate", expression = "java(LocalDateTime.now())")
    BpmnDiagramEntity toEntity(final BpmnDiagram model);

    @Mapping(target = "svgPreview", expression = "java((entity.getSvgPreview() == null) ? null : new String(entity.getSvgPreview()))")
    BpmnDiagramTO toTO(final BpmnDiagramEntity entity);
}
