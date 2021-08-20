package io.miragon.bpmrepo.core.repository.domain.mapper;

import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactEntity;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.RepositoryEntity;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(imports = LocalDateTime.class)
public interface RepositoryMapper {

    Repository mapToModel(final RepositoryEntity entity);

    RepositoryEntity mapToEntity(final Repository model);

    List<Repository> mapToModel(List<RepositoryEntity> artifacts);

    List<Artifact> mapArtifactEntitiesToModel(List<ArtifactEntity> artifacts);
}
