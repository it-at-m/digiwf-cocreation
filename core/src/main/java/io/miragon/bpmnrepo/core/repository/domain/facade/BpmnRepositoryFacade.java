package io.miragon.bpmnrepo.core.repository.domain.facade;

import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramVersionService;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpa;
import io.miragon.bpmnrepo.core.repository.api.transport.BpmnRepositoryRequestTO;
import io.miragon.bpmnrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmnrepo.core.repository.domain.business.AssignmentService;
import io.miragon.bpmnrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmnrepo.core.repository.domain.business.BpmnRepositoryService;
import io.miragon.bpmnrepo.core.repository.domain.exception.RepositoryNameAlreadyInUseException;
import io.miragon.bpmnrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmnrepo.core.repository.infrastructure.entity.BpmnRepositoryEntity;
import io.miragon.bpmnrepo.core.repository.infrastructure.repository.AssignmentJpa;
import io.miragon.bpmnrepo.core.repository.infrastructure.repository.BpmnRepoJpa;
import io.miragon.bpmnrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmnrepo.core.user.domain.business.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BpmnRepositoryFacade {
    private final BpmnRepositoryService bpmnRepositoryService;
    private final BpmnDiagramService bpmnDiagramService;
    private final AssignmentService assignmentService;
    private final UserService userService;
    private final AuthService authService;
    private final BpmnDiagramVersionService bpmnDiagramVersionService;
    private final BpmnRepoJpa bpmnRepoJpa;
    private final AssignmentJpa assignmentJpa;
    private final BpmnDiagramJpa bpmnDiagramJpa;

    public void createRepository(final NewBpmnRepositoryTO newBpmnRepositoryTO) {
        this.checkIfRepositoryNameIsAvailable(newBpmnRepositoryTO.getBpmnRepositoryName());
        final String bpmnRepositoryId = this.bpmnRepositoryService.createRepository(newBpmnRepositoryTO);
        this.assignmentService.createInitialAssignment(bpmnRepositoryId);
        log.debug("Successfully created new repository");
    }

    public void updateRepository(final String bpmnRepositoryId, final NewBpmnRepositoryTO newBpmnRepositoryTO) {
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.ADMIN);
        this.bpmnRepositoryService.updateRepository(bpmnRepositoryId, newBpmnRepositoryTO);
        log.debug("The repository has been updated");
    }

    private void checkIfRepositoryNameIsAvailable(final String bpmnRepositoryName) {
        final List<AssignmentEntity> assignmentList = this.assignmentJpa.findAssignmentEntitiesByAssignmentId_UserIdEquals(this.userService.getUserIdOfCurrentUser());
        for (final AssignmentEntity assignmentEntity : assignmentList) {
            final Optional<BpmnRepositoryEntity> assignedRepository = this.bpmnRepoJpa.findByBpmnRepositoryIdEquals(assignmentEntity.getAssignmentId().getBpmnRepositoryId());
            if (assignedRepository.isPresent()) {
                if (assignedRepository.get().getBpmnRepositoryName().equals(bpmnRepositoryName)) {
                    throw new RepositoryNameAlreadyInUseException();
                }
            }
        }
    }

    public BpmnRepositoryRequestTO getSingleRepository(final String repositoryId) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.bpmnRepositoryService.getSingleRepository(repositoryId);
    }

    public List<BpmnRepositoryRequestTO> getAllRepositories() {
        final String userId = this.userService.getUserIdOfCurrentUser();
        return this.assignmentService.getAllAssignedRepositoryIds(userId).stream()
                .map(bpmnRepositoryId -> this.bpmnRepositoryService.getSingleRepository(bpmnRepositoryId))
                //.map(bpmnRepositoryTO -> this.appendExistingDiagramsAndAssignedUsers(bpmnRepositoryTO.getBpmnRepositoryId()))
                .collect(Collectors.toList());
    }

    public void deleteRepository(final String bpmnRepositoryId) {
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.OWNER);
        this.bpmnDiagramVersionService.deleteAllByRepositoryId(bpmnRepositoryId);
        this.bpmnDiagramService.deleteAllByRepositoryId(bpmnRepositoryId);
        this.bpmnRepositoryService.deleteRepository(bpmnRepositoryId);
        this.assignmentService.deleteAllByRepositoryId(bpmnRepositoryId);
        log.debug("Deleted repository including related diagrams and assignments");
    }

/*    public BpmnRepositoryRequestTO appendExistingDiagramsAndAssignedUsers(String bpmnRepositoryId){
        Integer existingDiagrams = this.bpmnDiagramJpa.countAllByBpmnRepositoryId(bpmnRepositoryId);
        Integer assignedUsers = this.assignmentJpa.countByAssignmentId_BpmnRepositoryId(bpmnRepositoryId);
        BpmnRepositoryRequestTO bpmnRepositoryRequestTO =
    }*/
}
