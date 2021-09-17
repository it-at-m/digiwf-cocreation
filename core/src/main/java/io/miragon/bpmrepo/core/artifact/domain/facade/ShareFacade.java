package io.miragon.bpmrepo.core.artifact.domain.facade;

import io.miragon.bpmrepo.core.artifact.api.transport.SharedRepositoryTO;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ShareWithRepository;
import io.miragon.bpmrepo.core.artifact.domain.model.ShareWithTeam;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.ShareService;
import io.miragon.bpmrepo.core.repository.domain.facade.RepositoryFacade;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.repository.domain.service.RepositoryService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
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
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        if (artifact.getRepositoryId() == shareWithRepository.getRepositoryId()) {
            //TODO: Throw custom error
            throw new RuntimeException("Cant share with parent repo");
        }
        return this.shareService.shareWithRepository(shareWithRepository);
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

    public void unshareWithRepository(final String artifactId, final String repositoryId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        this.shareService.deleteShareWithRepository(artifactId, repositoryId);
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

    public List<Artifact> getArtifactsSharedWithRepository(final String repositoryId) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.MEMBER);

        final Repository repository = this.repositoryFacade.getRepository(repositoryId);
        final List<String> sharedArtifactIds = this.shareService.getSharedArtifactsFromRepository(repositoryId).stream().map(ShareWithRepository::getArtifactId).collect(Collectors.toList());
        return this.artifactService.getAllArtifactsById(sharedArtifactIds);
    }

    /*
    public List<Artifact> getArtifactsSharedWithTeam(final String repositoryId) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.MEMBER);

        //TODO: Use the Team instead of the repository here
        final Repository repository = this.repositoryFacade.getRepository(repositoryId);
        final List<String> sharedArtifactIds = this.shareService.getSharedArtifactsFromRepository(repositoryId).stream().map(ShareWithTeam::getArtifactId).collect(Collectors.toList());
        return this.artifactService.getAllArtifactsById(sharedArtifactIds);
    }
*/
    public List<SharedRepositoryTO> getSharedRepositories(final String artifactId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId);
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        final List<ShareWithRepository> shareWithRepositories = this.shareService.getSharedRepositories(artifactId);
        final List<SharedRepositoryTO> sharedRepositoryTOS = shareWithRepositories.stream().map(shareWithRepository -> {
            final SharedRepositoryTO sharedRepositoryTO = new SharedRepositoryTO(
                    shareWithRepository,
                    this.artifactService.getArtifactById(shareWithRepository.getArtifactId()).getName(),
                    this.repositoryService.getRepository(shareWithRepository.getRepositoryId()).getName());
            return sharedRepositoryTO;
        }).collect(Collectors.toList());
        return sharedRepositoryTOS;
    }
}
