package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.mapper;

import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.StarredEntity;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.StarredId;
import org.mapstruct.Mapper;

@Mapper
public interface StarredMapper {

    StarredEntity toEntity(final StarredId id);

    StarredId toEmbeddable(final String artifactId, final String userId);

}
