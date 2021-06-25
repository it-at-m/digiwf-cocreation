package io.miragon.bpmrepo.core.diagram.domain.mapper;

import io.miragon.bpmrepo.core.diagram.domain.model.DiagramVersion;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.DiagramVersionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface VersionMapper {

    DiagramVersionEntity mapToEntity(final DiagramVersion model);

    DiagramVersion mapToModel(final DiagramVersionEntity entity);

    List<DiagramVersion> mapToModel(List<DiagramVersionEntity> list);

}
