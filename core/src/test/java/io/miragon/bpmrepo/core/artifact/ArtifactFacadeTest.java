package io.miragon.bpmrepo.core.artifact;

import io.miragon.bpmrepo.core.artifact.domain.facade.ArtifactFacade;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactVersionService;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.repository.domain.service.RepositoryService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ArtifactFacadeTest {

    @InjectMocks
    private ArtifactFacade artifactFacade;

    @Mock
    private AuthService authService;

    @Mock
    private ArtifactService artifactService;
    @Mock
    private ArtifactVersionService artifactVersionService;
    @Mock
    private RepositoryService repositoryService;

    private static final String artifactId = "123456";
    private static final String REPOID = "01";
    private static final String artifactName = "TestArtifact";
    private static final String DIAGRAMDESC = "SomeDescription";
    private static final Integer EXISTINGDIAGRAMS = 5;
    private static LocalDateTime DATE;

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void createOrUpdateArtifact() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, artifactName, DIAGRAMDESC);

        this.artifactFacade.createArtifact(REPOID, artifact);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.MEMBER);

    }

    @Test
    public void getSingleArtifact() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, artifactName, DIAGRAMDESC, LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(artifact);

        this.artifactFacade.getArtifact(artifactId);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.artifactService, times(1)).getArtifactById(artifactId);
    }

    @Test
    public void deleteArtifact() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, artifactName, DIAGRAMDESC, LocalDateTime.now(), LocalDateTime.now());

        when(this.artifactService.countExistingArtifacts(REPOID)).thenReturn(EXISTINGDIAGRAMS);
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(artifact);

        this.artifactFacade.deleteArtifact(artifactId);

        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.ADMIN);
        verify(this.artifactVersionService, times(1)).deleteAllByArtifactId(artifactId);
        verify(this.artifactService, times(1)).deleteArtifact(artifactId);
        verify(this.repositoryService, times(1)).updateExistingArtifacts(REPOID, EXISTINGDIAGRAMS);
    }
}
