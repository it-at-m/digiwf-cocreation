package io.miragon.bpmrepo.core.diagram.domain.facade;

import io.miragon.bpmrepo.core.diagram.domain.business.DiagramService;
import io.miragon.bpmrepo.core.diagram.domain.business.DiagramVersionService;
import io.miragon.bpmrepo.core.diagram.domain.business.StarredService;
import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramUpdate;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.StarredEntity;
import io.miragon.bpmrepo.core.repository.domain.business.AssignmentService;
import io.miragon.bpmrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmrepo.core.repository.domain.business.RepositoryService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiagramFacade {
    private final AuthService authService;
    private final UserService userService;

    private final DiagramService diagramService;
    private final DiagramVersionService diagramVersionService;
    private final StarredService starredService;

    private final AssignmentService assignmentService;
    private final RepositoryService repositoryService;

    public Diagram createDiagram(final String repositoryId, final Diagram diagram) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.MEMBER);

        val result = this.diagramService.createDiagram(diagram);
        final Integer existingDiagrams = this.diagramService.countExistingDiagrams(repositoryId);
        this.repositoryService.updateExistingDiagrams(repositoryId, existingDiagrams);

        log.debug("Diagram created");
        return result;

    }

    public Diagram updateDiagram(final String diagramId, final DiagramUpdate diagramUpdate) {
        final Diagram diagram = this.diagramService.getDiagramById(diagramId);
        this.authService.checkIfOperationIsAllowed(diagram.getRepositoryId(), RoleEnum.MEMBER);

        val result = this.diagramService.updateDiagram(diagramId, diagramUpdate);

        log.debug("Diagram updated");
        return result;
    }

    public List<Diagram> getDiagramsFromRepo(final String repositoryId) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.diagramService.getDiagramsByRepo(repositoryId);
    }

    public Diagram getDiagram(final String diagramId) {
        final Diagram diagram = this.diagramService.getDiagramById(diagramId);
        this.authService.checkIfOperationIsAllowed(diagram.getRepositoryId(), RoleEnum.VIEWER);

        return diagram;
    }

    public List<Diagram> getRecent() {
        final List<String> assignments = this.assignmentService.getAllAssignedRepositoryIds(this.userService.getUserIdOfCurrentUser());
        return this.diagramService.getRecent(assignments);
    }

    public void updatePreviewSVG(final String diagramId, final String svgPreview) {
        final Diagram diagram = this.diagramService.getDiagramById(diagramId);
        this.authService.checkIfOperationIsAllowed(diagram.getRepositoryId(), RoleEnum.MEMBER);
        this.diagramService.updatePreviewSVG(diagramId, svgPreview);
    }

    public void deleteDiagram(final String diagramId) {
        final Diagram diagram = this.diagramService.getDiagramById(diagramId);
        this.authService.checkIfOperationIsAllowed(diagram.getRepositoryId(), RoleEnum.ADMIN);

        this.diagramVersionService.deleteAllByDiagramId(diagramId);
        this.diagramService.deleteDiagram(diagramId);

        final Integer existingDiagrams = this.diagramService.countExistingDiagrams(diagram.getRepositoryId());
        this.repositoryService.updateExistingDiagrams(diagram.getRepositoryId(), existingDiagrams);
    }

    public void setStarred(final String diagramId, final String userId) {
        final Diagram diagram = this.diagramService.getDiagramById(diagramId);
        this.authService.checkIfOperationIsAllowed(diagram.getRepositoryId(), RoleEnum.VIEWER);
        this.starredService.setStarred(diagramId, userId);
    }

    public List<Diagram> getStarred() {
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final List<StarredEntity> starredList = this.starredService.getStarred(currentUserId);
        return starredList.stream()
                .map(starredEntity -> this.diagramService.getDiagramById(starredEntity.getId().getDiagramId()))
                .collect(Collectors.toList());
    }

    public List<Diagram> searchDiagrams(final String typedTitle) {
        final String currentUserId = this.userService.getUserIdOfCurrentUser();
        final List<String> assignedRepoIds = this.assignmentService.getAllAssignedRepositoryIds(currentUserId);
        final List<Diagram> diagramList = this.diagramService.searchDiagrams(assignedRepoIds, typedTitle);
        return diagramList;
    }
}
