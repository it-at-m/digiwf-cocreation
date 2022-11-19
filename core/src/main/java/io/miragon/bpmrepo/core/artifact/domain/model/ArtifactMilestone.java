package io.miragon.bpmrepo.core.artifact.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Builder
@ToString
@AllArgsConstructor
public class ArtifactMilestone {

    private String id;

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

    public ArtifactMilestone(final String artifactId, final String repositoryId, final String file, final String comment) {
        this.repositoryId = repositoryId;
        this.artifactId = artifactId;
        this.comment = comment;
        this.file = file;
        this.updatedDate = LocalDateTime.now();
        this.latestMilestone = true;
        this.deployments = new ArrayList<>();
    }

    public void setOutdated() {
        this.latestMilestone = false;
    }

    //TODO: Change the saveType here if additional entities (besides one entity for each milestone) should be saved and change some more code in facade and service
    public void updateVersion(final ArtifactMilestoneUpdate artifactMilestoneUpdate) {
        this.comment = (artifactMilestoneUpdate.getComment() == null ? this.comment : artifactMilestoneUpdate.getComment());
        this.file = artifactMilestoneUpdate.getFile();
        this.updatedDate = LocalDateTime.now();
    }


    public void updateMilestoneNumber(final Integer milestone) {
        this.milestone = milestone;
    }

    public void deploy(final NewDeployment newDeployment, final String user) {
        final Deployment deployment = new Deployment(newDeployment, user);
        this.deployments.add(deployment);
    }
//
//    public void updateDeployment(final Deployment deployment, final String user) {
//        //Deployment must be passed in here
//        //just adjust User and Timestamp
//        //File cannot be edited after it has been deployed once -> this method is not callable right now. Causes problems if a user wants to deploy multiple files at once -> if one is already deployed, an exception is thrown and the whole group deployment fails
//        final Deployment updatedDeployment = this.deployments.stream().filter(existingDeployments -> existingDeployments.getId().equals(deployment.getId())).findFirst().orElseThrow(() -> new ObjectNotFoundException("exception.versionNotFound"));
//        updatedDeployment.setUser(user);
//        updatedDeployment.setTimestamp(LocalDateTime.now());
//    }

}
