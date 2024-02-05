package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.service;

import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.StarredEntity;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.StarredId;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.repository.StarredJpaRepository;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.mapper.StarredMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StarredService {

    private final StarredJpaRepository starredJpa;
    private final StarredMapper mapper;

    public void setStarred(final String artifactId, final String userId) {
        final Optional<StarredEntity> starredEntity = this.starredJpa.findById_artifactIdAndId_UserId(artifactId, userId);
        if (starredEntity.isEmpty()) {
            log.debug("Persisting the starred-relation");
            this.createStarred(artifactId, userId);
            return;
        }
        log.debug("Deleting the starred-relation");
        this.deleteStarred(artifactId, userId);
    }

    public void createStarred(final String artifactId, final String userId) {
        final StarredId starredId = this.mapper.toEmbeddable(artifactId, userId);
        final StarredEntity starredEntity = this.mapper.toEntity(starredId);
        this.starredJpa.save(starredEntity);
    }

    public void deleteStarred(final String artifactId, final String userId) {
        this.starredJpa.deleteById_artifactIdAndId_UserId(artifactId, userId);
    }

    public List<StarredEntity> getStarred(final String userId) {
        log.debug("Querying all starred objects");
        return this.starredJpa.findAllById_UserId(userId);
    }

    public void deleteAllByArtifactIds(final List<String> artifactIds) {
        final int deletedRelations = this.starredJpa.deleteAllById_artifactIdIn(artifactIds);
        log.debug(String.format("Deleted %s starred-relations", deletedRelations));
    }
}
