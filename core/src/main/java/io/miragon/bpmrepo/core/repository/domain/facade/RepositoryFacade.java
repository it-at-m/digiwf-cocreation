package io.miragon.bpmrepo.core.repository.domain.facade;

import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactMilestoneService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.StarredService;
import io.miragon.bpmrepo.core.repository.domain.model.NewRepository;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.model.RepositoryUpdate;
import io.miragon.bpmrepo.core.repository.domain.service.AssignmentService;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.repository.domain.service.RepositoryService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.NameConflictException;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public Optional<Repository> getRepository(final String repositoryId) {
        log.debug("Get Repository {}", repositoryId);
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
                .map(id -> this.repositoryService.getRepository(id).orElseThrow(() -> new ObjectNotFoundException("exception.repositoryNotFound")))
                .collect(Collectors.toList());
    }

    public void deleteRepository(final String repositoryId) {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.OWNER);
        this.artifactMilestoneService.deleteAllByRepositoryId(repositoryId);
        final List<Artifact> artifacts = this.artifactService.getArtifactsByRepo(repositoryId);
        if (!artifacts.isEmpty()) {
            this.starredService.deleteAllByArtifactIds(artifacts.stream().map(Artifact::getId).collect(Collectors.toList()));
        }
        this.artifactService.deleteAllByRepositoryId(repositoryId);
        this.repositoryService.deleteRepository(repositoryId);
        this.assignmentService.deleteAllByRepositoryId(repositoryId);
    }

    public List<Repository> searchRepositories(final String typedName) {
        return this.repositoryService.searchRepositories(typedName);
    }


    public HttpHeaders getHeaders(final String repositoryId) {
        final Repository repository = this.repositoryService.getRepository(repositoryId).orElseThrow(() -> new ObjectNotFoundException("exception.repositoryNotFound"));
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; fileName=%s.zip", repository.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return headers;
    }

    public ByteArrayResource download(final String repositoryId) throws RuntimeException {
        log.debug("Checking Permissions");
        this.authService.checkIfOperationIsAllowed(repositoryId, RoleEnum.MEMBER);
        final List<Artifact> allArtifacts = this.artifactService.getArtifactsByRepo(repositoryId);
        return this.zipArtifacts(allArtifacts);
    }


    //------------------------------ HELPER METHODS ------------------------------//

    private void checkIfRepositoryNameIsAvailable(final String repositoryName, final String userId) {
        final List<String> assignedRepositoryIds = this.assignmentService.getAllAssignedRepositoryIds(userId);
        for (final String repositoryId : assignedRepositoryIds) {
            final Repository repository = this.repositoryService.getRepository(repositoryId).orElseThrow(() -> new ObjectNotFoundException("exception.repositoryNotFound"));
            if (repository.getName().equals(repositoryName)) {
                throw new NameConflictException("exception.repositoryNameInUse");
            }
        }
    }

    private ByteArrayResource zipArtifacts(final List<Artifact> artifacts) throws RuntimeException {
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ZipOutputStream zipOut = new ZipOutputStream(bos);
            for (final Artifact artifact : artifacts) {
                final ArtifactMilestone artifactMilestone = this.artifactMilestoneService.getLatestMilestone(artifact.getId());
                log.debug("zipping {}", artifact.getName());
                //create an empty file(ZipEntry) and add it to the zipfile
                final ZipEntry zipEntry = new ZipEntry(artifact.getName() + this.getFileExtension(artifact.getFileType()));
                zipOut.putNextEntry(zipEntry);
                //fill the file with the Byte-content of the artifacts latest milestone
                zipOut.write(artifactMilestone.getFile().getBytes(), 0, artifactMilestone.getFile().getBytes().length);
            }
            zipOut.close();
            bos.close();
            return new ByteArrayResource(bos.toByteArray());
        } catch (final Exception e) {
            log.error("failed to zip artifacts; " + e.getMessage(), e);
            throw new RuntimeException("failed to zip artifacts");
        }
    }

    private String getFileExtension(final String fileType) {
        if (fileType.equalsIgnoreCase("ELEMENT_TEMPLATE") || fileType.equalsIgnoreCase("CONFIGURATION")) {
            return ".json";
        }
        return "." + fileType.toLowerCase();
    }

}
