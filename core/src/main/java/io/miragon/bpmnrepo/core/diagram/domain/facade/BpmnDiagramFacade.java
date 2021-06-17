package io.miragon.bpmnrepo.core.diagram.domain.facade;


import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramSVGUploadTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramVersionService;
import io.miragon.bpmnrepo.core.diagram.domain.business.StarredService;
import io.miragon.bpmnrepo.core.diagram.domain.exception.DiagramNameAlreadyInUseException;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.StarredEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpa;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.StarredJpa;
import io.miragon.bpmnrepo.core.repository.domain.business.AssignmentService;
import io.miragon.bpmnrepo.core.repository.domain.business.BpmnRepositoryService;
import io.miragon.bpmnrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmnrepo.core.diagram.domain.business.VerifyRelationService;
import io.miragon.bpmnrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmnrepo.core.user.domain.business.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BpmnDiagramFacade {
    private final AuthService authService;
    private final VerifyRelationService verifyRelationService;
    private final UserService userService;

    private final BpmnDiagramService bpmnDiagramService;
    private final BpmnDiagramVersionService bpmnDiagramVersionService;
    private final StarredService starredService;

    private final BpmnDiagramJpa bpmnDiagramJpa;
    private final StarredJpa starredJpa;

    private final AssignmentService assignmentService;
    private final BpmnRepositoryService bpmnRepositoryService;


    public BpmnDiagramTO createOrUpdateDiagram(String bpmnRepositoryId, BpmnDiagramUploadTO bpmnDiagramUploadTO) {
        authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.MEMBER);
        BpmnDiagramTO bpmnDiagramTO = new BpmnDiagramTO(bpmnRepositoryId, bpmnDiagramUploadTO);
        if (bpmnDiagramTO.getBpmnDiagramId() == null || bpmnDiagramTO.getBpmnDiagramId().isEmpty()) {
            checkIfNameIsAvailable(bpmnRepositoryId, bpmnDiagramTO.getBpmnDiagramName());
            val result = bpmnDiagramService.createDiagram(bpmnDiagramTO);
            Integer existingDiagrams = this.bpmnDiagramService.countExistingDiagrams(bpmnRepositoryId);
            bpmnRepositoryService.updateExistingDiagrams(bpmnRepositoryId, existingDiagrams);
            log.debug("Diagram created");
            return result;
        } else {
            verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnDiagramTO);
            val result = bpmnDiagramService.updateDiagram(bpmnDiagramTO);

            log.debug("Diagram updated");
            return result;
        }
    }

    public void checkIfNameIsAvailable(String bpmnRepositoryId, String bpmnDiagramName) {
        if (bpmnDiagramJpa.findBpmnDiagramEntityByBpmnRepositoryIdAndBpmnDiagramName(bpmnRepositoryId, bpmnDiagramName) != null) {
            throw new DiagramNameAlreadyInUseException();
        }
    }

    public List<BpmnDiagramTO> getDiagramsFromRepo(String repositoryId) {
        authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return bpmnDiagramService.getDiagramsFromRepo(repositoryId);
    }

    public BpmnDiagramTO getSingleDiagram(String bpmnRepositoryId, String bpmnDiagramId) {
        verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.VIEWER);
        return bpmnDiagramService.getSingleDiagram(bpmnDiagramId);
    }

    public List<BpmnDiagramTO> getRecent() {
        // List<Assignment> assignments = this.assignmentJpa.findAssignmentEntitiesByAssignmentId_UserIdEquals(this.userService.getUserIdOfCurrentUser());
        List<String> assignments = this.assignmentService.getAllAssignedRepositoryIds(this.userService.getUserIdOfCurrentUser());
        //pass assignments to diagramService and return all diagramTOs
        return this.bpmnDiagramService.getRecent(assignments);
    }


    public void updatePreviewSVG(String bpmnRepositoryId, String bpmnDiagramId, BpmnDiagramSVGUploadTO bpmnDiagramSVGUploadTO) {
        authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.MEMBER);
        verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        this.bpmnDiagramService.updatePreviewSVG(bpmnDiagramId, bpmnDiagramSVGUploadTO);

    }


    public void deleteDiagram(String bpmnRepositoryId, String bpmnDiagramId) {
        verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.ADMIN);
        bpmnDiagramVersionService.deleteAllByDiagramId(bpmnDiagramId);
        bpmnDiagramService.deleteDiagram(bpmnDiagramId);
        Integer existingDiagrams = this.bpmnDiagramService.countExistingDiagrams(bpmnRepositoryId);
        bpmnRepositoryService.updateExistingDiagrams(bpmnRepositoryId, existingDiagrams);

    }


    public void setStarred(String bpmnDiagramId) {
        BpmnDiagramEntity bpmnDiagramEntity = this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId);
        authService.checkIfOperationIsAllowed(bpmnDiagramEntity.getBpmnRepositoryId(), RoleEnum.VIEWER);
        String currentUserId = this.userService.getUserIdOfCurrentUser();
        StarredEntity starredEntity = this.starredJpa.findByStarredId_BpmnDiagramIdAndStarredId_UserId(bpmnDiagramId, currentUserId);
        if (starredEntity == null) {
            this.starredService.createStarred(bpmnDiagramId, currentUserId);
        } else {
            this.starredService.deleteStarred(bpmnDiagramId, currentUserId);
        }
    }

    public List<BpmnDiagramTO> getStarred() {
        String currentUserId = this.userService.getUserIdOfCurrentUser();
        List<StarredEntity> starredList = this.starredService.getStarred(currentUserId);
        return starredList.stream()
                .map(starredEntity -> this.bpmnDiagramService.getSingleDiagram(starredEntity.getStarredId().getBpmnDiagramId()))
                .collect(Collectors.toList());

    }
}
