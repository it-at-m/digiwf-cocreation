package io.miragon.bpmrepo.core.artifact.domain.business;

import io.miragon.bpmrepo.core.artifact.infrastructure.repository.ArtifactVersionJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyRelationService {
    private final ArtifactVersionJpaRepository artifactVersionJpaRepository;

    public boolean checkIfVersionIsInitialVersion(final String artifactId) {
        return this.artifactVersionJpaRepository
                .findFirstByArtifactIdOrderByMilestoneDesc(artifactId).isEmpty();
    }
}
