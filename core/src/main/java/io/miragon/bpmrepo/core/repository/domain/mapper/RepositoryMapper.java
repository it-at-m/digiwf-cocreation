package io.miragon.bpmrepo.core.repository.domain.mapper;

import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.RepositoryEntity;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(imports = LocalDateTime.class)
public interface RepositoryMapper {

    Repository mapToModel(final RepositoryEntity entity);

    RepositoryEntity mapToEntity(final Repository model);
}
