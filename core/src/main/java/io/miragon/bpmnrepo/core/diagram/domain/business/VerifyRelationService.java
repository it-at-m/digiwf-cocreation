package io.miragon.bpmnrepo.core.diagram.domain.business;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramVersionEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpa;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramVersionJpa;
import io.miragon.bpmnrepo.core.shared.exception.AccessRightException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyRelationService {
    private final BpmnDiagramJpa bpmnDiagramJpa;
    private final BpmnDiagramVersionJpa bpmnDiagramVersionJpa;

    public void verifyDiagramIsInSpecifiedRepository(BpmnDiagramTO bpmnDiagramTO) {
        String bpmnRepositoryId = bpmnDiagramTO.getBpmnRepositoryId();
        String bpmnDiagramId = bpmnDiagramTO.getBpmnDiagramId();
        BpmnDiagramEntity bpmnDiagramEntity = bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId);
        if (!bpmnDiagramEntity.getBpmnRepositoryId().equals(bpmnRepositoryId)) {
            throw new AccessRightException("This Diagram does not belong to the specified Repository");
        }
    }

    public void verifyDiagramIsInSpecifiedRepository(String bpmnRepositoryId, String bpmnDiagramId){
        BpmnDiagramEntity bpmnDiagramEntity = bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId);
        if(!bpmnDiagramEntity.getBpmnRepositoryId().equals(bpmnRepositoryId)){
            throw new AccessRightException("This Diagram does not belong to the specified Repository");
        }
    }

    public void verifyVersionIsFromSpecifiedDiagram(String bpmnDiagramId, String bpmnDiagramVersionId) {
        BpmnDiagramVersionEntity bpmnDiagramVersionEntity = bpmnDiagramVersionJpa.findAllByBpmnDiagramVersionIdEquals(bpmnDiagramVersionId);
        if (!bpmnDiagramVersionEntity.getBpmnDiagramId().equals(bpmnDiagramId)) {
            throw new AccessRightException("This version does not belong to the specified Diagram");
        }
    }

    public boolean checkIfVersionIsInitialVersion(String bpmnDiagramId){
        BpmnDiagramVersionEntity bpmnDiagramVersionEntity = bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(bpmnDiagramId);
        if(bpmnDiagramVersionEntity == null){
            return true;
        }
        else{
            return false;
        }
    }
}
