package de.muenchen.oss.digiwf.cocreation.core.artifact.model;

import de.muenchen.oss.digiwf.cocreation.core.artifact.ArtifactBuilder;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Artifact;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactUpdate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class ArtifactModelTest {

    private static final String REPOID = "42";
    private static final String artifactId = "001";
    private static final String artifactName = "artifact name";
    private static final String NEWartifactName = "new name";
    private static final String DIAGRAMDESC = "artifact description";
    private static final String NEWDIAGRAMDESC = "new description";
    private static LocalDateTime DATE;
    private static final String FILE_TYPE = "BPMN";

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void updateArtifact() {
        final ArtifactUpdate artifactTO = ArtifactBuilder.buildArtifactUpdate(NEWartifactName, NEWDIAGRAMDESC);
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, artifactName, DIAGRAMDESC, FILE_TYPE, DATE, DATE);

        //update
        artifact.updateArtifact(artifactTO);
        assertEquals(NEWartifactName, artifact.getName());
        assertEquals(NEWDIAGRAMDESC, artifact.getDescription());
        assertNotEquals(DATE, artifact.getUpdatedDate());
    }

}
