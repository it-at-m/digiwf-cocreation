package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.mapper;

import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Artifact;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.ArtifactEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(imports = LocalDateTime.class)
public interface ArtifactMapper {

    Artifact mapToModel(final ArtifactEntity entity);

    List<Artifact> mapToModel(final List<ArtifactEntity> list);

    @Mapping(target = "createdDate", expression = "java((model.getCreatedDate() == null) ? LocalDateTime.now() : model.getCreatedDate())")
    @Mapping(target = "updatedDate", expression = "java(LocalDateTime.now())")
    ArtifactEntity mapToEntity(final Artifact model);

}
