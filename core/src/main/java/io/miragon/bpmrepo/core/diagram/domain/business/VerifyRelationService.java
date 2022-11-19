package io.miragon.bpmrepo.core.diagram.domain.business;

import io.miragon.bpmrepo.core.diagram.infrastructure.repository.DiagramVersionJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyRelationService {
    private final DiagramVersionJpaRepository diagramVersionJpaRepository;

    public boolean checkIfVersionIsInitialVersion(final String diagramId) {
        return this.diagramVersionJpaRepository
                .findFirstByDiagramIdOrderByReleaseDescMilestoneDesc(diagramId).isEmpty();
    }
}
