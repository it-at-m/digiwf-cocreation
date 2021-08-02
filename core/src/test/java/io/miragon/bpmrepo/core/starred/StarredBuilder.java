package io.miragon.bpmrepo.core.starred;

import io.miragon.bpmrepo.core.artifact.domain.model.Starred;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.StarredEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.StarredId;

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
