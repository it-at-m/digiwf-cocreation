package io.miragon.bpmnrepo.core.diagram.domain.mapper;

import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.StarredEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.StarredId;
import org.mapstruct.Mapper;

@Mapper
public interface StarredMapper {

    StarredEntity toEntity(final StarredId starredId);

    StarredId toEmbeddable(final String bpmnDiagramId, final String userId);

}
