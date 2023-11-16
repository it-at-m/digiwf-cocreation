package de.muenchen.oss.digiwf.cocreation.core.artifact;

import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.facade.ArtifactFacade;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Artifact;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.service.ArtifactMilestoneService;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.service.ArtifactService;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.service.AuthService;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.service.RepositoryService;
import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@Disabled
public class ArtifactFacadeTest {

    @InjectMocks
    private ArtifactFacade artifactFacade;

    @Mock
    private AuthService authService;

    @Mock
    private ArtifactService artifactService;
    @Mock
    private ArtifactMilestoneService artifactMilestoneService;
    @Mock
    private RepositoryService repositoryService;

    private static final String artifactId = "123456";
    private static final String REPOID = "01";
    private static final String artifactName = "TestArtifact";
    private static final String DIAGRAMDESC = "SomeDescription";
    private static final Integer EXISTINGDIAGRAMS = 5;
    private static LocalDateTime DATE;
    private static final String FILE_TYPE = "BPMN";

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void createOrUpdateArtifact() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, artifactName, DIAGRAMDESC);

        this.artifactFacade.createArtifact(REPOID, artifact, null);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.MEMBER);

    }

    @Test
    public void getSingleArtifact() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, artifactName, DIAGRAMDESC, FILE_TYPE, LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(Optional.of(artifact));

        this.artifactFacade.getArtifact(artifactId);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.artifactService, times(1)).getArtifactById(artifactId);
    }

    @Test
    public void deleteArtifact() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, artifactName, DIAGRAMDESC, FILE_TYPE, LocalDateTime.now(), LocalDateTime.now());

        when(this.artifactService.countExistingArtifacts(REPOID)).thenReturn(EXISTINGDIAGRAMS);
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(Optional.of(artifact));

        this.artifactFacade.deleteArtifact(artifactId);

        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.ADMIN);
        verify(this.artifactMilestoneService, times(1)).deleteAllByArtifactId(artifactId);
        verify(this.artifactService, times(1)).deleteArtifact(artifactId);
        verify(this.repositoryService, times(1)).updateExistingArtifacts(REPOID, EXISTINGDIAGRAMS);
    }
}
