package io.miragon.bpmrepo.core.artifact.domain.model;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Getter
@Builder
@ToString
@AllArgsConstructor
public class ArtifactVersion {

    private final String id;

    private SaveTypeEnum saveType;

    private final String artifactId;

    private final String repositoryId;

    private Integer milestone;

    private String file;

    private String comment;

    private boolean latestVersion;

    private LocalDateTime updatedDate;

    private final List<Deployment> deployments;


    public void increaseVersion(final ArtifactVersion artifactVersion) {
        this.comment = artifactVersion.getComment();
        this.milestone++;
        this.file = artifactVersion.getFile();
        this.updatedDate = LocalDateTime.now();
    }

    public void setOutdated() {
        this.latestVersion = false;
    }

    //TODO: Change the saveType here if additional entities (besides one entity for each milestone) should be saved and change some more code in facade and service
    public void updateVersion(final ArtifactVersionUpdate artifactVersionUpdate) {
        this.comment = (artifactVersionUpdate.getComment() == null ? this.comment : artifactVersionUpdate.getComment());
        this.file = artifactVersionUpdate.getFile();
        this.updatedDate = LocalDateTime.now();
        this.saveType = SaveTypeEnum.MILESTONE;
    }


    public void updateMilestone(final Integer milestone) {
        this.milestone = milestone;
    }

    public void deploy(final String target, final String user) {
        final Deployment deployment = Deployment.builder()
                .target(target)
                .user(user)
                .timestamp(LocalDateTime.now())
                .build();
        this.deployments.add(deployment);
    }

    public void updateDeployment(final Deployment deployment, final String user) {
        //Deployment must be passed in here
        //just adjust User and Timestamp
        //File cannot be edited after it has been deployed once -> this method is not callable anymore
        final Deployment updatedDeployment = this.deployments.stream().filter(existingDeployments -> existingDeployments.getId().equals(deployment.getId())).findFirst().orElseThrow(() -> new ObjectNotFoundException("exception.versionNotFound"));
        updatedDeployment.setUser(user);
        updatedDeployment.setTimestamp(LocalDateTime.now());
    }

}
