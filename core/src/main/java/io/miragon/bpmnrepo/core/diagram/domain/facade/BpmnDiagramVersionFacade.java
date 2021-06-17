package io.miragon.bpmnrepo.core.diagram.domain.facade;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramVersionService;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmnrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmnrepo.core.diagram.domain.business.VerifyRelationService;
import io.miragon.bpmnrepo.core.shared.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BpmnDiagramVersionFacade {
    private final AuthService authService;
    private final VerifyRelationService verifyRelationService;
    private final BpmnDiagramVersionService bpmnDiagramVersionService;
    private final BpmnDiagramService bpmnDiagramService;


    public String createOrUpdateVersion(String bpmnRepositoryId, String bpmnDiagramId, BpmnDiagramVersionUploadTO bpmnDiagramVersionUploadTO){
        authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.MEMBER);
        verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        BpmnDiagramVersionTO bpmnDiagramVersionTO = new BpmnDiagramVersionTO(bpmnRepositoryId, bpmnDiagramId, bpmnDiagramVersionUploadTO);
        //true if it is the initial version
        if(verifyRelationService.checkIfVersionIsInitialVersion(bpmnDiagramId)){
            String bpmnDiagramVersionId = this.bpmnDiagramVersionService.createInitialVersion(bpmnDiagramVersionTO);
            this.bpmnDiagramService.updateUpdatedDate(bpmnDiagramId);
            return bpmnDiagramVersionId;
        }
        else{
            String bpmnDiagramVersionId = this.bpmnDiagramVersionService.updateVersion(bpmnDiagramVersionTO);
            //refresh the updated date in diagramEntity
            this.bpmnDiagramService.updateUpdatedDate(bpmnDiagramId);
            deleteAutosavedVersionsIfReleaseOrMilestoneIsSaved(bpmnRepositoryId, bpmnDiagramId, bpmnDiagramVersionUploadTO.getSaveType());
            return bpmnDiagramVersionId;
        }
    }


    //simply deletes all entities that contain the SaveType "AUTOSAVE"
    private void deleteAutosavedVersionsIfReleaseOrMilestoneIsSaved(String bpmnRepositoryId, String bpmnDiagramId, SaveTypeEnum saveTypeEnum) {
        if(saveTypeEnum.equals(SaveTypeEnum.AUTOSAVE)){
            this.bpmnDiagramVersionService.deleteAutosavedVersions(bpmnRepositoryId, bpmnDiagramId);
        }
    }


    public List<BpmnDiagramVersionTO> getAllVersions(String bpmnRepositoryId, String bpmnDiagramId){
        verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.VIEWER);
        return bpmnDiagramVersionService.getAllVersions(bpmnDiagramId);
    }

    public BpmnDiagramVersionTO getLatestVersion(String bpmnRepositoryId, String bpmnDiagramId){
        verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.VIEWER);
        return bpmnDiagramVersionService.getLatestVersion(bpmnDiagramId);
    }

    public BpmnDiagramVersionTO getSingleVersion(String bpmnRepositoryId, String bpmnDiagramId, String bpmnDiagramVersionId){
        verifyRelationService.verifyVersionIsFromSpecifiedDiagram(bpmnDiagramId, bpmnDiagramVersionId);
        verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.VIEWER);
        return bpmnDiagramVersionService.getSingleVersion(bpmnDiagramVersionId);
    }
}
