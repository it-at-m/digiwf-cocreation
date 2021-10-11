package io.miragon.bpmrepo.core.version.model;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.version.MilestoneBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class VersionModelTest {

    private static final String REPOID = "42";
    private static final String artifactId = "001";
    private static final String VERSIONID = "v-01";
    private static final String FILESTRING = "somexmlString";
    private static final Integer MILESTONE = 2;
    private static final String COMMENT = "versionComment";
    private static final String UPDATEDCOMMENT = "new comment";
    private static final SaveTypeEnum saveTypeMileStone = SaveTypeEnum.MILESTONE;

    @Test
    @Disabled
    public void updateVersion() {
        final ArtifactMilestone version = MilestoneBuilder
                .buildMilestone(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, saveTypeMileStone);
        final ArtifactMilestone artifactMilestoneUpdate = MilestoneBuilder
                .buildMilestone(VERSIONID, artifactId, REPOID, UPDATEDCOMMENT, MILESTONE, FILESTRING, saveTypeMileStone);

        //first update: MILESTONE - check version numbers
        version.increaseMilestone(artifactMilestoneUpdate);
        assertEquals(version.getMilestone(), MILESTONE + 1);
        //second update: MILESTONE - check version numbers
        version.increaseMilestone(artifactMilestoneUpdate);
        assertEquals(version.getMilestone(), MILESTONE + 2);
    }

}
