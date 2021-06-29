package io.miragon.bpmrepo.core.diagram.domain.facade;

import io.miragon.bpmrepo.core.diagram.domain.business.DiagramService;
import io.miragon.bpmrepo.core.diagram.domain.business.DiagramVersionService;
import io.miragon.bpmrepo.core.diagram.domain.business.VerifyRelationService;
import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramVersion;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramVersionUpload;
import io.miragon.bpmrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiagramVersionFacade {
    private final AuthService authService;
    private final VerifyRelationService verifyRelationService;
    private final DiagramVersionService diagramVersionService;
    private final DiagramService diagramService;

    public String createOrUpdateVersion(final String diagramId, final DiagramVersionUpload diagramVersionUpload) {
        //TODO refactoring - to complicated

        final Diagram diagram = this.diagramService.getDiagramById(diagramId);
        this.authService.checkIfOperationIsAllowed(diagram.getRepositoryId(), RoleEnum.MEMBER);

        final DiagramVersion diagramVersion = DiagramVersion.builder()
                .repositoryId(diagram.getRepositoryId())
                .diagramId(diagramId)
                .comment(diagramVersionUpload.getVersionComment())
                .xml(diagramVersionUpload.getXml())
                .saveType(diagramVersionUpload.getSaveType())
                .updatedDate(LocalDateTime.now())
                .build();

        //initial version
        if (this.verifyRelationService.checkIfVersionIsInitialVersion(diagramId)) {
            final String bpmnDiagramVersionId = this.diagramVersionService.createInitialVersion(diagramVersion);
            this.diagramService.updateUpdatedDate(diagramId);
            return bpmnDiagramVersionId;
        }

        //Update current version
        if (diagramVersionUpload.getSaveType() == SaveTypeEnum.AUTOSAVE) {
            final String bpmnDiagramVersionId = this.diagramVersionService.updateVersion(diagramVersion);
            //refresh the updated date in diagramEntity
            this.diagramService.updateUpdatedDate(diagramId);
            return bpmnDiagramVersionId;
        }

        //Create new Version
        final String bpmnDiagramVersionId = this.diagramVersionService.createNewVersion(diagramVersion);
        this.diagramService.updateUpdatedDate(diagramId);
        this.deleteAutosavedVersionsIfReleaseOrMilestoneIsSaved(diagram.getRepositoryId(), diagramId, diagramVersionUpload.getSaveType());
        return bpmnDiagramVersionId;
    }

    //simply deletes all entities that contain the SaveType "AUTOSAVE"
    private void deleteAutosavedVersionsIfReleaseOrMilestoneIsSaved(final String bpmnRepositoryId, final String bpmnDiagramId,
                                                                    final SaveTypeEnum saveTypeEnum) {
        if (saveTypeEnum.equals(SaveTypeEnum.AUTOSAVE)) {
            this.diagramVersionService.deleteAutosavedVersions(bpmnRepositoryId, bpmnDiagramId);
        }
    }

    public List<DiagramVersion> getAllVersions(final String diagramId) {
        final Diagram diagram = this.diagramService.getDiagramById(diagramId);
        this.authService.checkIfOperationIsAllowed(diagram.getRepositoryId(), RoleEnum.VIEWER);
        return this.diagramVersionService.getAllVersions(diagramId);
    }

    public DiagramVersion getLatestVersion(final String diagramId) {
        final Diagram diagram = this.diagramService.getDiagramById(diagramId);
        this.authService.checkIfOperationIsAllowed(diagram.getRepositoryId(), RoleEnum.VIEWER);
        return this.diagramVersionService.getLatestVersion(diagramId);
    }

    public DiagramVersion getVersion(final String diagramId, final String diagramVersionId) {
        final Diagram diagram = this.diagramService.getDiagramById(diagramId);
        this.authService.checkIfOperationIsAllowed(diagram.getRepositoryId(), RoleEnum.VIEWER);
        return this.diagramVersionService.getVersion(diagramVersionId);
    }
}
