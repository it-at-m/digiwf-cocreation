package io.miragon.bpmrepo.core.sharing.domain.facade;

import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.repository.domain.facade.RepositoryFacade;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.repository.domain.service.RepositoryService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.sharing.api.transport.SharedRepositoryTO;
import io.miragon.bpmrepo.core.sharing.domain.model.ShareWithRepository;
import io.miragon.bpmrepo.core.sharing.domain.model.ShareWithTeam;
import io.miragon.bpmrepo.core.sharing.domain.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShareFacade {

    private final AuthService authService;
    private final ArtifactService artifactService;
    private final ShareService shareService;
    private final RepositoryService repositoryService;
    private final RepositoryFacade repositoryFacade;

    public ShareWithRepository shareWithRepository(final ShareWithRepository shareWithRepository) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(shareWithRepository.getArtifactId());
        final Repository repository = this.repositoryFacade.getRepository(shareWithRepository.getRepositoryId());
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        if (artifact.getRepositoryId() == shareWithRepository.getRepositoryId()) {
            //TODO: Throw custom error
            throw new RuntimeException("Cant share with parent repo");
        }
        this.repositoryFacade.addShareRelation(repository, artifact);
        return this.shareService.shareWithRepository(shareWithRepository);
    }

    public void unshareWithRepository(final String artifactId, final String repositoryId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        final Repository repository = this.repositoryFacade.getRepository(repositoryId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        this.repositoryFacade.removeShareRelation(repository, artifact);
        this.shareService.deleteShareWithRepository(artifactId, repositoryId);
    }


    public ShareWithRepository updateShareWithRepository(final ShareWithRepository shareWithRepository) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(shareWithRepository.getArtifactId());
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        return this.shareService.updateShareWithRepository(shareWithRepository);
    }

    public ShareWithTeam shareWithTeam(final ShareWithTeam shareWithTeam) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(shareWithTeam.getArtifactId());
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        if (artifact.getRepositoryId() == shareWithTeam.getTeamId()) {
            //TODO: Throw custom error
            throw new RuntimeException("Cant share with parent repo");
        }
        return this.shareService.shareWithTeam(shareWithTeam);
    }

    public ShareWithTeam updateShareWithTeam(final ShareWithTeam shareWithTeam) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(shareWithTeam.getArtifactId());
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        return this.shareService.updateShareWithTeam(shareWithTeam);
    }


    public void unshareWithTeam(final String artifactId, final String teamId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        this.shareService.deleteShareWithTeam(artifactId, teamId);
    }


    public List<Artifact> getAllSharedArtifacts(final String userId) {
        log.debug("Checking Assignments");
        final List<Repository> repositories = this.repositoryFacade.getAllRepositories(userId);
        return this.shareService.getSharedArtifactsFromRepositories(repositories);
    }

    public List<Artifact> getSharedArtifactsByType(final String userId, final String type) {
        log.debug("Checking Assignments");
        final List<Repository> repositories = this.repositoryFacade.getAllRepositories(userId);
        return this.shareService.getSharedArtifactsFromRepositoriesByType(repositories, type);
    }

    public List<Artifact> getArtifactsSharedWithRepository(final String repositoryId) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        final List<ShareWithRepository> shareRelations = this.shareService.getSharedArtifactsFromRepository(repositoryId);
        final List<String> sharedArtifactIds = shareRelations.stream().map(shareRelation -> shareRelation.getArtifactId()).collect(Collectors.toList());
        return this.artifactService.getAllArtifactsById(sharedArtifactIds);
    }

    public List<Artifact> getArtifactsSharedWithRepositoryByType(final String repositoryId, final String type) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        final List<ShareWithRepository> shareRelations = this.shareService.getSharedArtifactsFromRepository(repositoryId);
        final List<String> sharedArtifactIds = shareRelations.stream().map(shareRelation -> shareRelation.getArtifactId()).collect(Collectors.toList());
        return this.artifactService.getAllArtifactsByIdAndType(sharedArtifactIds, type);
    }


    public List<SharedRepositoryTO> getSharedRepositories(final String artifactId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        final List<ShareWithRepository> shareWithRepositories = this.shareService.getSharedRepositories(artifactId);
        final List<SharedRepositoryTO> sharedRepositoryTOS = shareWithRepositories.stream().map(shareWithRepository -> {
            final SharedRepositoryTO sharedRepositoryTO = new SharedRepositoryTO(
                    shareWithRepository.getArtifactId(),
                    shareWithRepository.getRepositoryId(),
                    shareWithRepository.getRole(),
                    this.artifactService.getArtifactById(shareWithRepository.getArtifactId()).getName(),
                    this.repositoryService.getRepository(shareWithRepository.getRepositoryId()).getName());
            return sharedRepositoryTO;
        }).collect(Collectors.toList());
        return sharedRepositoryTOS;
    }


    //TODO: nach einführung von Teams wieder einfügen


}
