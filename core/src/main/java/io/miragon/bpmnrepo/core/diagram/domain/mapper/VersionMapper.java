package io.miragon.bpmnrepo.core.diagram.domain.mapper;

import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagramVersion;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionTO;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramVersionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VersionMapper {
    BpmnDiagramVersionEntity toEntity(final BpmnDiagramVersion model);

    BpmnDiagramVersion toModel(final BpmnDiagramVersionEntity entity);

    @Mapping(target = "bpmnAsXML", expression = "java(new String(entity.getBpmnDiagramVersionFile()))")
    BpmnDiagramVersionTO toTO(final BpmnDiagramVersionEntity entity);

}
