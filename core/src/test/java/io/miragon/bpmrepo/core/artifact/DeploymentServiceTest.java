package io.miragon.bpmrepo.core.artifact;

import io.miragon.bpmrepo.core.artifact.domain.enums.DeploymentStatus;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.artifact.domain.model.NewDeployment;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactMilestoneService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.DeploymentService;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DeploymentServiceTest {

    @Autowired
    private DeploymentService deploymentService;
    @Autowired
    private ArtifactService artifactService;
    @Autowired
    private ArtifactMilestoneService artifactMilestoneService;

    private static final String ARTIFACTID = "123456";
    private static final String REPOID = "01";
    private static final String ARTIFACTNAME = "TestArtifact";
    private static final String ARTIFACTDESC = "SomeDescription";
    private static final LocalDateTime DATE = LocalDateTime.now();
    private static final String USERNAME = "USER";
    private static final String FILE_TYPE = "BPMN";

    public Artifact createArtifact() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(ARTIFACTID, REPOID, ARTIFACTNAME, ARTIFACTDESC, FILE_TYPE, DATE, DATE);
        final Artifact createdArtifact = this.artifactService.createArtifact(artifact);
        assertNotNull(createdArtifact);
        return createdArtifact;
    }

    public ArtifactMilestone createArtifactMilestone() {
        ArtifactMilestone milestone = ArtifactMilestone.builder()
                .artifactId(ARTIFACTID)
                .repositoryId(REPOID)
                .deployments(new ArrayList<>())
                .build();
        milestone = this.artifactMilestoneService.createInitialMilestone(milestone);
        assertNotNull(milestone);
        return milestone;
    }

    @Test
    void createDeployment() {
        final Artifact artifact = this.createArtifact();
        final ArtifactMilestone artifactMilestone = this.createArtifactMilestone();

        final NewDeployment newDeployment = new NewDeployment(REPOID, ARTIFACTID, artifactMilestone.getId(), "LOCAL");
        final ArtifactMilestone deployedMilestone = this.deploymentService.deploy(artifactMilestone, newDeployment, artifact, USERNAME);

        // verify that deployment is saved in deployedMilestone
        for (Deployment deployment : deployedMilestone.getDeployments()) {
            Assertions.assertNotNull(deployment.getId());
            Assertions.assertEquals(newDeployment.getArtifactId(), deployment.getArtifactId());
            Assertions.assertEquals(newDeployment.getRepositoryId(), deployment.getRepositoryId());
            Assertions.assertEquals(newDeployment.getTarget(), deployment.getTarget());
            // check that status is pending
            Assertions.assertEquals(DeploymentStatus.PENDING, deployment.getStatus());

            // update deployment status
            final String statusMessage = "Deployment was successful";
            deployment = this.deploymentService.updateDeploymentStatus(
                    deployment.getId(),
                    DeploymentStatus.SUCCESS,
                    statusMessage
            );
            Assertions.assertEquals(DeploymentStatus.SUCCESS, deployment.getStatus());
            Assertions.assertEquals(statusMessage, deployment.getMessage());
        }
    }

    @Test
    void updateDeploymentStatusRaisesObjectNotFoundExceptionIfDeploymentDoesNotExist() {
        Assertions.assertThrows(ObjectNotFoundException.class, () -> this.deploymentService.updateDeploymentStatus(
                "not-existing-id",
                DeploymentStatus.SUCCESS,
                null
        ));
    }

}
