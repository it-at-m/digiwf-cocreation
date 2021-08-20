package io.miragon.bpmrepo.core.artifact.domain.mapper;

import io.miragon.bpmrepo.core.artifact.domain.model.Shared;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.SharedEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.SharedId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper
public interface SharedMapper {
    @Mapping(source = "model", target = ".")
    @Mapping(source = "sharedId", target = "sharedId")
    SharedEntity mapToEntity(Shared model, SharedId sharedId);

    @Mapping(source = "artifactId", target = "artifactId")
    @Mapping(source = "repositoryId", target = "repositoryId")
    SharedId mapRepoToEmbeddable(String artifactId, String repositoryId);

    @Mapping(source = "artifactId", target = "artifactId")
    @Mapping(source = "teamId", target = "teamId")
    SharedId mapTeamToEmbeddable(String artifactId, String teamId);

    @Mapping(source = "artifactId", target = "artifactId")
    @Mapping(source = "repositoryId", target = "repositoryId")
    @Mapping(source = "teamId", target = "teamId")
    SharedId mapToEmbeddable(String artifactId, String repositoryId, String teamId);

    @Mapping(target = "artifactId", expression = "java(sharedEntity.getSharedId().getArtifactId())")
    @Mapping(target = "repositoryId", expression = "java(sharedEntity.getSharedId().getRepositoryId())")
    @Mapping(target = "teamId", expression = "java(sharedEntity.getSharedId().getTeamId())")
    Shared mapToModel(SharedEntity sharedEntity);

    List<Shared> mapToModel(List<SharedEntity> list);
}
