package io.miragon.bpmrepo.core.diagram.domain.mapper;

import io.miragon.bpmrepo.core.diagram.infrastructure.entity.StarredEntity;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.StarredId;
import org.mapstruct.Mapper;

@Mapper
public interface StarredMapper {

    StarredEntity toEntity(final StarredId id);

    StarredId toEmbeddable(final String diagramId, final String userId);

}
