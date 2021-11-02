package io.miragon.bpmrepo.core.sharing;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.sharing.domain.model.ShareWithRepository;
import io.miragon.bpmrepo.core.sharing.infrastructure.entity.ShareWithRepositoryEntity;
import io.miragon.bpmrepo.core.sharing.infrastructure.entity.ShareWithRepositoryId;

public class SharingBuilder {

    public static ShareWithRepositoryEntity buildShareWithRepositoryEntity(final ShareWithRepositoryId shareWithRepositoryId, final RoleEnum role) {
        return ShareWithRepositoryEntity.builder()
                .shareWithRepositoryId(shareWithRepositoryId)
                .role(role)
                .build();
    }

    public static ShareWithRepository buildShareWithRepository(final String artifactId, final String repositoryId, final RoleEnum role) {
        return ShareWithRepository.builder()
                .artifactId(artifactId)
                .repositoryId(repositoryId)
                .role(role)
                .build();
    }
    
}
