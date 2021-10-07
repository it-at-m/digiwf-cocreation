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
public class ArtifactMilestone {

    private final String id;

    private SaveTypeEnum saveType;

    private final String artifactId;

    private final String repositoryId;

    private Integer milestone;

    private String file;

    private String comment;

    private boolean latestMilestone;

    private LocalDateTime updatedDate;

    private final List<Deployment> deployments;


    public void increaseMilestone(final ArtifactMilestone artifactMilestone) {
        this.comment = artifactMilestone.getComment();
        this.milestone++;
        this.file = artifactMilestone.getFile();
        this.updatedDate = LocalDateTime.now();
    }

    public void setOutdated() {
        this.latestMilestone = false;
    }

    //TODO: Change the saveType here if additional entities (besides one entity for each milestone) should be saved and change some more code in facade and service
    public void updateVersion(final ArtifactMilestoneUpdate artifactMilestoneUpdate) {
        this.comment = (artifactMilestoneUpdate.getComment() == null ? this.comment : artifactMilestoneUpdate.getComment());
        this.file = artifactMilestoneUpdate.getFile();
        this.updatedDate = LocalDateTime.now();
        this.saveType = SaveTypeEnum.MILESTONE;
    }


    public void updateMilestone(final Integer milestone) {
        this.milestone = milestone;
    }

    public void deploy(final NewDeployment newDeployment, final String user) {
        final Deployment deployment = Deployment.builder()
                .target(newDeployment.getTarget())
                .user(user)
                .timestamp(LocalDateTime.now())
                .repositoryId(newDeployment.getRepositoryId())
                .artifactId(newDeployment.getArtifactId())
                .build();
        this.deployments.add(deployment);
    }

    public void updateDeployment(final Deployment deployment, final String user) {
        //Deployment must be passed in here
        //just adjust User and Timestamp
        //File cannot be edited after it has been deployed once -> this method is not callable right now. Causes problems if a user wants to deploy multiple files at once -> if one is already deployed, an exception is thrown and the whole bulk deployment fails
        final Deployment updatedDeployment = this.deployments.stream().filter(existingDeployments -> existingDeployments.getId().equals(deployment.getId())).findFirst().orElseThrow(() -> new ObjectNotFoundException("exception.versionNotFound"));
        updatedDeployment.setUser(user);
        updatedDeployment.setTimestamp(LocalDateTime.now());
    }

}
