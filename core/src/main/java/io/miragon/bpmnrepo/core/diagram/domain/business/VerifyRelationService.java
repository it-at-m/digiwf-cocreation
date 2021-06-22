package io.miragon.bpmnrepo.core.diagram.domain.business;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramVersionEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpaRepository;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramVersionJpaRepository;
import io.miragon.bpmnrepo.core.shared.exception.AccessRightException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyRelationService {
    private final BpmnDiagramJpaRepository bpmnDiagramJpa;
    private final BpmnDiagramVersionJpaRepository bpmnDiagramVersionJpa;

    public void verifyDiagramIsInSpecifiedRepository(final BpmnDiagramTO bpmnDiagramTO) {
        final String bpmnRepositoryId = bpmnDiagramTO.getBpmnRepositoryId();
        final String bpmnDiagramId = bpmnDiagramTO.getBpmnDiagramId();
        final BpmnDiagramEntity bpmnDiagramEntity = this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId);
        if (!bpmnDiagramEntity.getBpmnRepositoryId().equals(bpmnRepositoryId)) {
            throw new AccessRightException("This Diagram does not belong to the specified Repository");
        }
    }

    public void verifyDiagramIsInSpecifiedRepository(final String bpmnRepositoryId, final String bpmnDiagramId) {
        final BpmnDiagramEntity bpmnDiagramEntity = this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId);
        if (!bpmnDiagramEntity.getBpmnRepositoryId().equals(bpmnRepositoryId)) {
            throw new AccessRightException("This Diagram does not belong to the specified Repository");
        }
    }

    public void verifyVersionIsFromSpecifiedDiagram(final String bpmnDiagramId, final String bpmnDiagramVersionId) {
        final BpmnDiagramVersionEntity bpmnDiagramVersionEntity = this.bpmnDiagramVersionJpa.findAllByBpmnDiagramVersionIdEquals(bpmnDiagramVersionId);
        if (!bpmnDiagramVersionEntity.getBpmnDiagramId().equals(bpmnDiagramId)) {
            throw new AccessRightException("This version does not belong to the specified Diagram");
        }
    }

    public boolean checkIfVersionIsInitialVersion(final String bpmnDiagramId) {
        final BpmnDiagramVersionEntity bpmnDiagramVersionEntity = this.bpmnDiagramVersionJpa
                .findFirstByBpmnDiagramIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(bpmnDiagramId);
        if (bpmnDiagramVersionEntity == null) {
            return true;
        } else {
            return false;
        }
    }
}
