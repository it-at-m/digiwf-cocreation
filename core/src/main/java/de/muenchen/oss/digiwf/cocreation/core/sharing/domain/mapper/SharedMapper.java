package de.muenchen.oss.digiwf.cocreation.core.sharing.domain.mapper;

import de.muenchen.oss.digiwf.cocreation.core.sharing.domain.model.ShareWithRepository;
import de.muenchen.oss.digiwf.cocreation.core.sharing.infrastructure.entity.ShareWithRepositoryEntity;
import de.muenchen.oss.digiwf.cocreation.core.sharing.infrastructure.entity.ShareWithRepositoryId;
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
