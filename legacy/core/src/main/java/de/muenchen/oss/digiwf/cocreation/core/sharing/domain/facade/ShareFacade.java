package de.muenchen.oss.digiwf.cocreation.core.sharing.domain.facade;

import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Artifact;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.service.ArtifactService;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.facade.RepositoryFacade;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.Repository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.service.AuthService;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.service.RepositoryService;
import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.ObjectNotFoundException;
import de.muenchen.oss.digiwf.cocreation.core.sharing.api.transport.SharedRepositoryTO;
import de.muenchen.oss.digiwf.cocreation.core.sharing.domain.model.ShareWithRepository;
import de.muenchen.oss.digiwf.cocreation.core.sharing.domain.service.ShareService;
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
        final Artifact artifact = this.artifactService.getArtifactById(shareWithRepository.getArtifactId()).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        if (artifact.getRepositoryId().equals(shareWithRepository.getRepositoryId())) {
            //TODO: Throw custom error
            throw new RuntimeException("exception.cantShareWithParentRepo");
        }
        return this.shareService.shareWithRepository(shareWithRepository);
    }

    public void unshareWithRepository(final String artifactId, final String repositoryId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        this.shareService.deleteShareWithRepository(artifactId, repositoryId);
    }

    public ShareWithRepository updateShareWithRepository(final ShareWithRepository shareWithRepository) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(shareWithRepository.getArtifactId()).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        return this.shareService.updateShareWithRepository(shareWithRepository);
    }

    public List<Artifact> getAllSharedArtifacts(final String userId) {
        log.debug("Checking Assignments");
        final List<String> repositoryIds = this.repositoryFacade.getAllRepositories(userId).stream().map(Repository::getId).collect(Collectors.toList());
        final List<String> artifactIds = this.shareService.getSharedArtifactIdsFromRepositories(repositoryIds);
        return this.artifactService.getAllArtifactsById(artifactIds);
    }

    public List<Artifact> getSharedArtifactsByType(final String userId, final String type) {
        log.debug("Checking Assignments");
        final List<String> repositoryIds = this.repositoryFacade.getAllRepositories(userId).stream().map(Repository::getId).collect(Collectors.toList());
        final List<String> artifactIds = this.shareService.getSharedArtifactIdsFromRepositories(repositoryIds);
        return this.artifactService.getAllArtifactsByIdAndType(artifactIds, type);
    }

    public List<Artifact> getArtifactsSharedWithRepository(final String repositoryId) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        final List<ShareWithRepository> shareRelations = this.shareService.getSharedArtifactsFromRepository(repositoryId);
        final List<String> sharedArtifactIds = shareRelations.stream().map(ShareWithRepository::getArtifactId).collect(Collectors.toList());
        return this.artifactService.getAllArtifactsById(sharedArtifactIds);
    }

    public List<Artifact> getArtifactsSharedWithRepositoryByType(final String repositoryId, final String type) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.VIEWER);
        final List<ShareWithRepository> shareRelations = this.shareService.getSharedArtifactsFromRepository(repositoryId);
        final List<String> sharedArtifactIds = shareRelations.stream().map(ShareWithRepository::getArtifactId).collect(Collectors.toList());
        return this.artifactService.getAllArtifactsByIdAndType(sharedArtifactIds, type);
    }


    public List<SharedRepositoryTO> getSharedRepositories(final String artifactId) {
        log.debug("Checking Permissions");
        final Artifact artifact = this.artifactService.getArtifactById(artifactId).orElseThrow(() -> new ObjectNotFoundException("exception.artifactNotFound"));
        this.authService.checkIfOperationIsAllowed(artifact.getRepositoryId(), RoleEnum.ADMIN);
        final List<ShareWithRepository> shareWithRepositories = this.shareService.getSharedRepositories(artifactId);
        //Add the repository- and artifact names to the TOs to avoid sending additional requests from client
        return shareWithRepositories.stream().map(shareWithRepository ->
                new SharedRepositoryTO(
                        shareWithRepository.getArtifactId(),
                        shareWithRepository.getRepositoryId(),
                        shareWithRepository.getRole(),
                        this.artifactService.getArtifactById(shareWithRepository.getArtifactId()).get().getName(),
                        this.repositoryService.getRepository(shareWithRepository.getRepositoryId())
                                .orElseThrow(() -> new ObjectNotFoundException("exception.repositoryNotFound")).getName())
        ).collect(Collectors.toList());
    }
}
