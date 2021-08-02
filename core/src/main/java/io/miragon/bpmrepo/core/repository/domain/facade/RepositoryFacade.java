package io.miragon.bpmrepo.core.repository.domain.facade;

import io.miragon.bpmrepo.core.artifact.domain.business.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.business.ArtifactVersionService;
import io.miragon.bpmrepo.core.artifact.domain.business.StarredService;
import io.miragon.bpmrepo.core.repository.domain.business.AssignmentService;
import io.miragon.bpmrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmrepo.core.repository.domain.business.RepositoryService;
import io.miragon.bpmrepo.core.repository.domain.exception.RepositoryNameAlreadyInUseException;
import io.miragon.bpmrepo.core.repository.domain.model.NewRepository;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.model.RepositoryUpdate;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepositoryFacade {
    private final RepositoryService repositoryService;
    private final ArtifactService artifactService;
    private final AssignmentService assignmentService;
    private final UserService userService;
    private final AuthService authService;
    private final ArtifactVersionService artifactVersionService;
    private final StarredService starredService;

    public Repository createRepository(final NewRepository newRepository) {
        this.checkIfRepositoryNameIsAvailable(newRepository.getName());
        final Repository repository = this.repositoryService.createRepository(newRepository);
        this.assignmentService.createInitialAssignment(repository.getId());
        return repository;
    }

    public void updateRepository(final String repositoryId, final RepositoryUpdate repositoryUpdate) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.ADMIN);
        this.repositoryService.updateRepository(repositoryId, repositoryUpdate);
    }

    private void checkIfRepositoryNameIsAvailable(final String repositoryName) {
        final List<String> assignedRepositoryIds = this.assignmentService.getAllAssignedRepositoryIds(this.userService.getUserIdOfCurrentUser());
        for (final String repositoryId : assignedRepositoryIds) {
            final Repository repository = this.repositoryService.getRepository(repositoryId);
            if (repository.getName().equals(repositoryName)) {
                throw new RepositoryNameAlreadyInUseException();
            }
        }
    }

    public Repository getRepository(final String repositoryId) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.repositoryService.getRepository(repositoryId);
    }

    public List<Repository> getAllRepositories() {
        final String userId = this.userService.getUserIdOfCurrentUser();
        return this.assignmentService.getAllAssignedRepositoryIds(userId).stream()
                .map(this.repositoryService::getRepository)
                .collect(Collectors.toList());
    }

    public void deleteRepository(final String repositoryId) {
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.OWNER);
        this.artifactVersionService.deleteAllByRepositoryId(repositoryId);

        final List<String> artifactIds = this.artifactService.getArtifactsByRepo(repositoryId).stream().map(artifact -> artifact.getId()).collect(Collectors.toList());
        this.starredService.deleteAllByArtifactIds(artifactIds);
        this.artifactService.deleteAllByRepositoryId(repositoryId);
        this.repositoryService.deleteRepository(repositoryId);
        this.assignmentService.deleteAllByRepositoryId(repositoryId);
        log.debug("Deleted repository including related artifacts and assignments");
    }
}
