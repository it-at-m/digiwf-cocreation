package de.muenchen.oss.digiwf.cocreation.core.sharing;

import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import de.muenchen.oss.digiwf.cocreation.core.sharing.domain.model.ShareWithRepository;
import de.muenchen.oss.digiwf.cocreation.core.sharing.infrastructure.entity.ShareWithRepositoryEntity;
import de.muenchen.oss.digiwf.cocreation.core.sharing.infrastructure.entity.ShareWithRepositoryId;

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
