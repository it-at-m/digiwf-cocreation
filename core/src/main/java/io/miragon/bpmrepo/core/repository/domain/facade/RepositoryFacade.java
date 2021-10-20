package io.miragon.bpmrepo.core.repository.domain.facade;

import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactMilestoneService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.StarredService;
import io.miragon.bpmrepo.core.repository.domain.mapper.RepositoryMapper;
import io.miragon.bpmrepo.core.repository.domain.model.NewRepository;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.model.RepositoryUpdate;
import io.miragon.bpmrepo.core.repository.domain.service.AssignmentService;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.repository.domain.service.RepositoryService;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.RepositoryEntity;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.NameConflictException;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepositoryFacade {
    private final RepositoryService repositoryService;
    private final ArtifactService artifactService;
    private final AssignmentService assignmentService;
    private final AuthService authService;
    private final ArtifactMilestoneService artifactMilestoneService;
    private final StarredService starredService;
    private final RepositoryMapper mapper;

    public Repository createRepository(final NewRepository newRepository, final String userId) {
        log.debug("Checking if name is available");
        this.checkIfRepositoryNameIsAvailable(newRepository.getName(), userId);
        final Repository repository = this.repositoryService.createRepository(newRepository);
        this.assignmentService.createInitialAssignment(repository.getId());
        return repository;
    }

    public Repository updateRepository(final String repositoryId, final RepositoryUpdate repositoryUpdate) {
        log.debug("Checking permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.ADMIN);
        return this.repositoryService.updateRepository(repositoryId, repositoryUpdate);
    }


    private void checkIfRepositoryNameIsAvailable(final String repositoryName, final String userId) {
        final List<String> assignedRepositoryIds = this.assignmentService.getAllAssignedRepositoryIds(userId);
        for (final String repositoryId : assignedRepositoryIds) {
            final Repository repository = this.mapper.mapToModel(this.repositoryService.getRepository(repositoryId).orElseThrow(() -> new ObjectNotFoundException("exception.repositoryNotFound")));
            if (repository.getName().equals(repositoryName)) {
                throw new NameConflictException("exception.repositoryNameInUse");
            }
        }
    }

    public Optional<RepositoryEntity> getRepository(final String repositoryId) {
        log.debug("No Permisisons required");
        //this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        return this.repositoryService.getRepository(repositoryId);
    }

    public List<Repository> getManageableRepositories(final String userId) {
        log.debug("Checking Assignments");
        final List<String> repositoryIds = this.assignmentService.getManageableRepositoryIds(userId);
        return this.repositoryService.getRepositories(repositoryIds);
    }

    public List<Repository> getAllRepositories(final String userId) {
        log.debug("Checking Assignments");
        return this.assignmentService.getAllAssignedRepositoryIds(userId).stream()
                .map(id -> this.mapper.mapToModel(this.repositoryService.getRepository(id).orElseThrow(() -> new ObjectNotFoundException("exception.repositoryNotFound"))))
                .collect(Collectors.toList());
    }

    public void deleteRepository(final String repositoryId) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.OWNER);
        this.artifactMilestoneService.deleteAllByRepositoryId(repositoryId);
        final List<Artifact> artifacts = this.artifactService.getArtifactsByRepo(repositoryId);
        if (artifacts.size() > 0) {
            this.starredService.deleteAllByArtifactIds(artifacts.stream().map(Artifact::getId).collect(Collectors.toList()));
        }
        this.artifactService.deleteAllByRepositoryId(repositoryId);
        this.repositoryService.deleteRepository(repositoryId);
        this.assignmentService.deleteAllByRepositoryId(repositoryId);
    }

    public List<Repository> searchRepositories(final String typedName) {
        return this.repositoryService.searchRepositories(typedName);
    }


}
