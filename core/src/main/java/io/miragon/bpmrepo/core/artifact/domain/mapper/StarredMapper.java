package io.miragon.bpmrepo.core.artifact.domain.mapper;

import io.miragon.bpmrepo.core.artifact.infrastructure.entity.StarredEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.StarredId;
import org.mapstruct.Mapper;

@Mapper
public interface StarredMapper {

    StarredEntity toEntity(final StarredId id);

    StarredId toEmbeddable(final String artifactId, final String userId);

}
