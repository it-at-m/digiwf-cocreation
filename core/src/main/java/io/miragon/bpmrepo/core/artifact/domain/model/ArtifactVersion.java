package io.miragon.bpmrepo.core.artifact.domain.model;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
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

    private final SaveTypeEnum saveType;

    private final String artifactId;

    private final String repositoryId;

    private Integer milestone;

    private String xml;

    private String comment;

    private boolean latestVersion;

    private LocalDateTime updatedDate;

    private final List<Deployment> deployments;


    public void increaseVersion(final ArtifactVersion artifactVersion) {
        this.comment = artifactVersion.getComment();
        this.milestone++;
        this.xml = artifactVersion.getXml();
        this.updatedDate = LocalDateTime.now();
    }

    public void setOutdated() {
        this.latestVersion = false;
    }

    public void updateVersion(final ArtifactVersionUpdate artifactVersionUpdate) {
        this.comment = artifactVersionUpdate.getComment();
        this.xml = artifactVersionUpdate.getXml();
        this.updatedDate = LocalDateTime.now();
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
}
