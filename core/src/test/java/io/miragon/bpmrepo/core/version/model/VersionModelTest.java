package io.miragon.bpmrepo.core.version.model;

import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramVersion;
import io.miragon.bpmrepo.core.version.VersionBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class VersionModelTest {

    private static final String REPOID = "42";
    private static final String DIAGRAMID = "001";
    private static final String VERSIONID = "v-01";
    private static final String FILESTRING = "somexmlString";
    private static final Integer RELEASE = 1;
    private static final Integer MILESTONE = 2;
    private static final String COMMENT = "versionComment";
    private static final String UPDATEDCOMMENT = "new comment";
    private static final SaveTypeEnum saveTypeRelease = SaveTypeEnum.RELEASE;
    private static final SaveTypeEnum saveTypeMileStone = SaveTypeEnum.MILESTONE;

    @Test
    @Disabled
    public void updateVersion() {
        final DiagramVersion version = VersionBuilder
                .buildVersion(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, saveTypeMileStone);
        final DiagramVersion diagramVersionUpdate = VersionBuilder
                .buildVersion(VERSIONID, DIAGRAMID, REPOID, UPDATEDCOMMENT, RELEASE, MILESTONE, FILESTRING, saveTypeMileStone);

        //first update: MILESTONE - check version numbers
        version.updateVersion(diagramVersionUpdate);
        assertEquals(version.getRelease(), RELEASE);
        assertEquals(version.getMilestone(), MILESTONE + 1);
        //second update: MILESTONE - check version numbers
        version.updateVersion(diagramVersionUpdate);
        assertEquals(version.getRelease(), RELEASE);
        assertEquals(version.getMilestone(), MILESTONE + 3);

        final DiagramVersion newVersion = VersionBuilder
                .buildVersion(VERSIONID, DIAGRAMID, REPOID, null, RELEASE, MILESTONE, FILESTRING, saveTypeRelease);

        //third update: RELEASE - check version numbers and if the old comment has been adopted
        version.updateVersion(newVersion);
        assertEquals(RELEASE + 1, version.getRelease());
        assertEquals(0, version.getMilestone());
        assertEquals(UPDATEDCOMMENT, version.getComment());

    }

}
