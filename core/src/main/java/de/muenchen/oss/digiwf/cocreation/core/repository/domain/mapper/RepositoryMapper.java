package de.muenchen.oss.digiwf.cocreation.core.repository.domain.mapper;

import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.RepositoryEntity;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.Repository;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RepositoryMapper {

    Repository mapToModel(final RepositoryEntity entity);

    RepositoryEntity mapToEntity(final Repository model);

    List<Repository> mapToModel(List<RepositoryEntity> artifacts);

}
