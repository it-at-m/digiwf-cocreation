package io.miragon.bpmnrepo.core.diagram.domain.facade;


import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramSVGUploadTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramVersionService;
import io.miragon.bpmnrepo.core.diagram.domain.business.StarredService;
import io.miragon.bpmnrepo.core.diagram.domain.business.VerifyRelationService;
import io.miragon.bpmnrepo.core.diagram.domain.exception.DiagramNameAlreadyInUseException;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.StarredEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpa;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.StarredJpa;
import io.miragon.bpmnrepo.core.repository.domain.business.AssignmentService;
import io.miragon.bpmnrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmnrepo.core.repository.domain.business.BpmnRepositoryService;
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


    public BpmnDiagramTO createOrUpdateDiagram(final String bpmnRepositoryId, final BpmnDiagramUploadTO bpmnDiagramUploadTO) {
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.MEMBER);
        final BpmnDiagramTO bpmnDiagramTO = new BpmnDiagramTO(bpmnRepositoryId, bpmnDiagramUploadTO);
        if (bpmnDiagramTO.getBpmnDiagramId() == null || bpmnDiagramTO.getBpmnDiagramId().isEmpty()) {
            this.checkIfNameIsAvailable(bpmnRepositoryId, bpmnDiagramTO.getBpmnDiagramName());
            val result = this.bpmnDiagramService.createDiagram(bpmnDiagramTO);
            final Integer existingDiagrams = this.bpmnDiagramService.countExistingDiagrams(bpmnRepositoryId);
            this.bpmnRepositoryService.updateExistingDiagrams(bpmnRepositoryId, existingDiagrams);
            log.debug("Diagram created");
            return result;
        } else {
            this.verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnDiagramTO);
            val result = this.bpmnDiagramService.updateDiagram(bpmnDiagramTO);

            log.debug("Diagram updated");
            return result;
        }
    }

    public void checkIfNameIsAvailable(final String bpmnRepositoryId, final String bpmnDiagramName) {
        if (this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnRepositoryIdAndBpmnDiagramName(bpmnRepositoryId, bpmnDiagramName) != null) {
            throw new DiagramNameAlreadyInUseException();
        }
    }

    public List<BpmnDiagramTO> getDiagramsFromRepo(final String repositoryId) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.bpmnDiagramService.getDiagramsFromRepo(repositoryId);
    }

    public BpmnDiagramTO getSingleDiagram(final String bpmnRepositoryId, final String bpmnDiagramId) {
        this.verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.VIEWER);
        return this.bpmnDiagramService.getSingleDiagram(bpmnDiagramId);
    }

    public List<BpmnDiagramTO> getRecent() {
        // List<Assignment> assignments = this.assignmentJpa.findAssignmentEntitiesByAssignmentId_UserIdEquals(this.userService.getUserIdOfCurrentUser());
        final List<String> assignments = this.assignmentService.getAllAssignedRepositoryIds(this.userService.getUserIdOfCurrentUser());
        //pass assignments to diagramService and return all diagramTOs
        return this.bpmnDiagramService.getRecent(assignments);
    }


    public void updatePreviewSVG(final String bpmnRepositoryId, final String bpmnDiagramId, final BpmnDiagramSVGUploadTO bpmnDiagramSVGUploadTO) {
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.MEMBER);
        this.verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        this.bpmnDiagramService.updatePreviewSVG(bpmnDiagramId, bpmnDiagramSVGUploadTO);

    }


    public void deleteDiagram(final String bpmnRepositoryId, final String bpmnDiagramId) {
        this.verifyRelationService.verifyDiagramIsInSpecifiedRepository(bpmnRepositoryId, bpmnDiagramId);
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.ADMIN);
        this.bpmnDiagramVersionService.deleteAllByDiagramId(bpmnDiagramId);
        this.bpmnDiagramService.deleteDiagram(bpmnDiagramId);
        final Integer existingDiagrams = this.bpmnDiagramService.countExistingDiagrams(bpmnRepositoryId);
        this.bpmnRepositoryService.updateExistingDiagrams(bpmnRepositoryId, existingDiagrams);

    }


    public void setStarred(final String bpmnDiagramId) {
        final BpmnDiagramEntity bpmnDiagramEntity = this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId);
        this.authService.checkIfOperationIsAllowed(bpmnDiagramEntity.getBpmnRepositoryId(), RoleEnum.VIEWER);
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final StarredEntity starredEntity = this.starredJpa.findByStarredId_BpmnDiagramIdAndStarredId_UserId(bpmnDiagramId, currentUserId);
        if (starredEntity == null) {
            this.starredService.createStarred(bpmnDiagramId, currentUserId);
        } else {
            this.starredService.deleteStarred(bpmnDiagramId, currentUserId);
        }
    }

    public List<BpmnDiagramTO> getStarred() {
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final List<StarredEntity> starredList = this.starredService.getStarred(currentUserId);
        return starredList.stream()
                .map(starredEntity -> this.bpmnDiagramService.getSingleDiagram(starredEntity.getStarredId().getBpmnDiagramId()))
                .collect(Collectors.toList());

    }

    public List<BpmnDiagramTO> searchDiagrams(final String typedTitle) {
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final List<String> assignedRepoIds = this.assignmentService.getAllAssignedRepositoryIds(currentUserId);
        final List<BpmnDiagramTO> diagramList = this.bpmnDiagramService.searchDiagrams(assignedRepoIds, typedTitle);
        return diagramList;
    }
}
