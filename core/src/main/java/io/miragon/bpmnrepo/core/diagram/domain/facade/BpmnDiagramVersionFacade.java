package io.miragon.bpmnrepo.core.diagram.domain.facade;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramVersionService;
import io.miragon.bpmnrepo.core.diagram.domain.business.VerifyRelationService;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmnrepo.core.repository.domain.business.AuthService;
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


    public String createOrUpdateVersion(final String bpmnRepositoryId, final String bpmnDiagramId, final BpmnDiagramVersionUploadTO bpmnDiagramVersionUploadTO) {
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.MEMBER);
        this.verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        final BpmnDiagramVersionTO bpmnDiagramVersionTO = new BpmnDiagramVersionTO(bpmnRepositoryId, bpmnDiagramId, bpmnDiagramVersionUploadTO);
        //true if it is the initial version
        if (this.verifyRelationService.checkIfVersionIsInitialVersion(bpmnDiagramId)) {
            final String bpmnDiagramVersionId = this.bpmnDiagramVersionService.createInitialVersion(bpmnDiagramVersionTO);
            this.bpmnDiagramService.updateUpdatedDate(bpmnDiagramId);
            return bpmnDiagramVersionId;
        } else {
            if (bpmnDiagramVersionTO.getSaveType() == SaveTypeEnum.AUTOSAVE) {
                final String bpmnDiagramVersionId = this.bpmnDiagramVersionService.updateVersion(bpmnDiagramVersionTO);
                //refresh the updated date in diagramEntity
                this.bpmnDiagramService.updateUpdatedDate(bpmnDiagramId);
                return bpmnDiagramVersionId;
            } else {
                //Create new Version
                log.warn("in else clause, diagramId: " + bpmnDiagramId);
                final String bpmnDiagramVersionId = this.bpmnDiagramVersionService.createNewVersion(bpmnDiagramVersionTO);
                log.warn("Created Version");
                this.bpmnDiagramService.updateUpdatedDate(bpmnDiagramId);
                this.deleteAutosavedVersionsIfReleaseOrMilestoneIsSaved(bpmnRepositoryId, bpmnDiagramId, bpmnDiagramVersionUploadTO.getSaveType());
                return bpmnDiagramVersionId;
            }
        }
    }


    //simply deletes all entities that contain the SaveType "AUTOSAVE"
    private void deleteAutosavedVersionsIfReleaseOrMilestoneIsSaved(final String bpmnRepositoryId, final String bpmnDiagramId, final SaveTypeEnum saveTypeEnum) {
        if (saveTypeEnum.equals(SaveTypeEnum.AUTOSAVE)) {
            this.bpmnDiagramVersionService.deleteAutosavedVersions(bpmnRepositoryId, bpmnDiagramId);
        }
    }


    public List<BpmnDiagramVersionTO> getAllVersions(final String bpmnRepositoryId, final String bpmnDiagramId) {
        this.verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.VIEWER);
        return this.bpmnDiagramVersionService.getAllVersions(bpmnDiagramId);
    }

    public BpmnDiagramVersionTO getLatestVersion(final String bpmnRepositoryId, final String bpmnDiagramId) {
        this.verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.VIEWER);
        return this.bpmnDiagramVersionService.getLatestVersion(bpmnDiagramId);
    }

    public BpmnDiagramVersionTO getSingleVersion(final String bpmnRepositoryId, final String bpmnDiagramId, final String bpmnDiagramVersionId) {
        this.verifyRelationService.verifyVersionIsFromSpecifiedDiagram(bpmnDiagramId, bpmnDiagramVersionId);
        this.verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.VIEWER);
        return this.bpmnDiagramVersionService.getSingleVersion(bpmnDiagramVersionId);
    }
}
