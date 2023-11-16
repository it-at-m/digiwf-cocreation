package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.service;

import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.repository.ArtifactMilestoneJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyRelationService {
    private final ArtifactMilestoneJpaRepository artifactMilestoneJpaRepository;

    public boolean checkIfMilestoneIsInitialMilestone(final String artifactId) {
        return this.artifactMilestoneJpaRepository
                .findFirstByArtifactIdOrderByMilestoneDesc(artifactId).isEmpty();
    }
}
