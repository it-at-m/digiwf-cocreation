package de.muenchen.oss.digiwf.cocreation.core.starred;

import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Starred;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.StarredEntity;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.StarredId;

public class StarredBuilder {

    public static StarredEntity buildStarredEntity(final StarredId embeddedId) {
        return StarredEntity.builder()
                .id(embeddedId)
                .build();
    }

    public static Starred buildStarred(final String artifactId, final String userId) {
        return Starred.builder()
                .artifactId(artifactId)
                .userId(userId)
                .build();
    }
}
