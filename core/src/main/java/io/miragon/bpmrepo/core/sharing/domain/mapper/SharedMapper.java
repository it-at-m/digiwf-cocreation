package io.miragon.bpmrepo.core.sharing.domain.mapper;

import io.miragon.bpmrepo.core.sharing.domain.model.ShareWithRepository;
import io.miragon.bpmrepo.core.sharing.infrastructure.entity.ShareWithRepositoryEntity;
import io.miragon.bpmrepo.core.sharing.infrastructure.entity.ShareWithRepositoryId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper
public interface SharedMapper {
    @Mapping(source = "model", target = ".")
    ShareWithRepositoryEntity mapShareWithRepositoryToEntity(ShareWithRepository model, ShareWithRepositoryId shareWithRepositoryId);

    @Mapping(source = "artifactId", target = "artifactId")
    @Mapping(source = "repositoryId", target = "repositoryId")
    ShareWithRepositoryId mapShareWithRepoIdToEmbeddable(String artifactId, String repositoryId);

    @Mapping(target = "artifactId", expression = "java(shareWithRepositoryEntity.getShareWithRepositoryId().getArtifactId())")
    @Mapping(target = "repositoryId", expression = "java(shareWithRepositoryEntity.getShareWithRepositoryId().getRepositoryId())")
    ShareWithRepository mapShareWithRepositoryToModel(ShareWithRepositoryEntity shareWithRepositoryEntity);

    List<ShareWithRepository> mapShareWithRepositoryToModel(List<ShareWithRepositoryEntity> list);
}
