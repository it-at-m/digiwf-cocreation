package io.miragon.bpmrepo.core.artifact.adapter;

import io.miragon.bpmrepo.core.artifact.ArtifactBuilder;
import io.miragon.bpmrepo.core.artifact.domain.enums.DeploymentStatus;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.Deployment;
import io.miragon.bpmrepo.core.artifact.domain.model.NewDeployment;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactMilestoneService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.DeploymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DeploymentAdapterTest {
    @Autowired
    private DeploymentAdapterImpl deploymentAdapter;

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

    private Deployment deployment;

    @BeforeEach
    void setUp() {
        // create artifact
        final Artifact artifact = ArtifactBuilder.buildArtifact(ARTIFACTID, REPOID, ARTIFACTNAME, ARTIFACTDESC, FILE_TYPE, DATE, DATE);
        final Artifact createdArtifact = this.artifactService.createArtifact(artifact);
        assertNotNull(createdArtifact);

        // create milestone
        ArtifactMilestone milestone = ArtifactMilestone.builder()
                .artifactId(ARTIFACTID)
                .repositoryId(REPOID)
                .deployments(new ArrayList<>())
                .build();
        milestone = this.artifactMilestoneService.createInitialMilestone(milestone);
        assertNotNull(milestone);

        // create deployment
        final NewDeployment newDeployment = new NewDeployment(REPOID, ARTIFACTID, milestone.getId(), "LOCAL");
        final ArtifactMilestone deployedMilestone = this.deploymentService.deploy(milestone, newDeployment, artifact, USERNAME);

        this.deployment = deployedMilestone.getDeployments().stream().findFirst().orElseThrow();
    }

    @Test
    void deploymentAdapter() {
        Deployment updatedDeployment = this.deploymentAdapter.successfulDeployment(this.deployment.getId());
        Assertions.assertEquals(DeploymentStatus.SUCCESS, updatedDeployment.getStatus());
        Assertions.assertNotNull(updatedDeployment.getMessage());

        updatedDeployment = this.deploymentAdapter.failedDeployment(this.deployment.getId());
        Assertions.assertEquals(DeploymentStatus.ERROR, updatedDeployment.getStatus());
        Assertions.assertNotNull(updatedDeployment.getMessage());
    }

}
