package io.miragon.bpmrepo.core.version;

import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestoneUpdate;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactMilestoneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class MilestoneServiceTest {


    @Autowired
    private ArtifactMilestoneService artifactMilestoneService;


    private static final String VERSIONID = "v01";
    private static final String ARTIFACTID = "123456";
    private static final String REPOID = "01";
    private static final String COMMENT = "VersionComment";
    private static final String COMMENT2 = "secondVersionComment";
    private static final String COMMENT2_1 = "updatedVersionComment";
    private static final String FILE = "<xml>abc</xml>";
    private static final String FILE2 = "<xml>abcdef</xml>";
    private static final String FILE2_1 = "<xml>abcdefgeh</xml>";


    private final ArtifactMilestone milestone = MilestoneBuilder.buildMilestone(VERSIONID, ARTIFACTID, REPOID, COMMENT, 0, FILE);
    private final ArtifactMilestone secondMilestone = MilestoneBuilder.buildMilestone(VERSIONID, ARTIFACTID, REPOID, COMMENT2, 0, FILE2);

    @Test
    public void tests() {
        final ArtifactMilestone milestone = this.createInitial();
        final ArtifactMilestone milestone2 = this.createSecondMilestone(milestone);
        this.updateMilestone(milestone2);
    }

    @Transactional
    public ArtifactMilestone createInitial() {

        final ArtifactMilestone createdMilestone = this.artifactMilestoneService.createInitialMilestone(this.milestone);
        assertNotNull(createdMilestone);
        //Id is generated and overwrites the passed value (but has to be passed here to start the test inside the service)
        assertNotEquals(VERSIONID, createdMilestone.getId());
        assertEquals(1, createdMilestone.getMilestone());
        return createdMilestone;
    }

    @Transactional
    public ArtifactMilestone createSecondMilestone(final ArtifactMilestone firstMilestone) {
        final ArtifactMilestone milestone2 = this.artifactMilestoneService.createNewMilestone(this.secondMilestone);
        assertNotNull(milestone2);
        assertNotEquals(firstMilestone.getId(), milestone2.getId());
        assertNotEquals(firstMilestone.getComment(), milestone2.getComment());
        assertNotEquals(firstMilestone.getFile(), milestone2.getFile());
        assertEquals(2, milestone2.getMilestone());
        assertEquals(firstMilestone.getRepositoryId(), milestone2.getRepositoryId());
        assertEquals(firstMilestone.getArtifactId(), milestone2.getArtifactId());
        return milestone2;
    }

    @Transactional
    public void updateMilestone(final ArtifactMilestone milestone2) {
        final ArtifactMilestoneUpdate milestoneUpdate = MilestoneBuilder.buildMilestoneUpdate(milestone2.getId(), COMMENT2_1, FILE2_1);
        final ArtifactMilestone updatedMilestone = this.artifactMilestoneService.updateMilestone(milestoneUpdate);
        assertNotEquals(milestone2.getComment(), milestoneUpdate.getComment());
        assertNotEquals(milestone2.getFile(), milestoneUpdate.getFile());
        assertEquals(milestone2.getId(), updatedMilestone.getId());
        assertEquals(2, updatedMilestone.getMilestone());
        assertEquals(milestone2.getRepositoryId(), updatedMilestone.getRepositoryId());
        assertEquals(milestone2.getArtifactId(), updatedMilestone.getArtifactId());
    }
}
