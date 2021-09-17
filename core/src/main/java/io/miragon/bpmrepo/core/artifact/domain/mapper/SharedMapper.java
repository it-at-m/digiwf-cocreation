package io.miragon.bpmrepo.core.artifact.domain.mapper;

import io.miragon.bpmrepo.core.artifact.domain.model.ShareWithRepository;
import io.miragon.bpmrepo.core.artifact.domain.model.ShareWithTeam;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ShareWithRepositoryEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ShareWithRepositoryId;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ShareWithTeamEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ShareWithTeamId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper
public interface SharedMapper {
    @Mapping(source = "model", target = ".")
    ShareWithRepositoryEntity mapShareWithRepositoryToEntity(ShareWithRepository model, ShareWithRepositoryId shareWithRepositoryId);

    @Mapping(source = "model", target = ".")
    ShareWithTeamEntity mapShareWithTeamToEntity(ShareWithTeam model, ShareWithTeamId shareWithTeamId);


    @Mapping(source = "artifactId", target = "artifactId")
    @Mapping(source = "repositoryId", target = "repositoryId")
    ShareWithRepositoryId mapShareWithRepoIdToEmbeddable(String artifactId, String repositoryId);

    @Mapping(source = "artifactId", target = "artifactId")
    @Mapping(source = "teamId", target = "teamId")
    ShareWithTeamId mapShareWithTeamIdToEmbeddable(String artifactId, String teamId);

    @Mapping(target = "artifactId", expression = "java(shareWithRepositoryEntity.getShareWithRepositoryId().getArtifactId())")
    @Mapping(target = "repositoryId", expression = "java(shareWithRepositoryEntity.getShareWithRepositoryId().getRepositoryId())")
    ShareWithRepository mapShareWithRepositoryToModel(ShareWithRepositoryEntity shareWithRepositoryEntity);

    @Mapping(target = "artifactId", expression = "java(shareWithTeamEntity.getShareWithTeamId().getArtifactId())")
    @Mapping(target = "teamId", expression = "java(shareWithTeamEntity.getShareWithTeamId().getTeamId())")
    ShareWithTeam mapShareWithTeamToModel(ShareWithTeamEntity shareWithTeamEntity);

    //List<Shared> mapShareWithRepositoryToModel(List<ShareWithRepositoryEntity> list);

    List<ShareWithRepository> mapShareWithRepositoryToModel(List<ShareWithRepositoryEntity> list);

    List<ShareWithTeam> mapShareWithTeamToModel(List<ShareWithTeamEntity> list);
}
