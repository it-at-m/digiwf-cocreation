package io.miragon.bpmrepo.core.version;

import io.miragon.bpmrepo.core.artifact.ArtifactBuilder;
import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.facade.ArtifactVersionFacade;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersionUpload;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactVersionService;
import io.miragon.bpmrepo.core.artifact.domain.service.VerifyRelationService;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VersionFacadeTest {

    @InjectMocks
    private ArtifactVersionFacade artifactVersionFacade;

    @Mock
    private AuthService authService;

    @Mock
    private VerifyRelationService verifyRelationService;

    @Mock
    private ArtifactVersionService artifactVersionService;

    @Mock
    private ArtifactService artifactService;

    private static final String REPOID = "42";
    private static final String artifactId = "001";
    private static final String VERSIONID = "v-01";
    private static final String REPONAME = "repo name";
    private static final String REPODESC = "repository description";
    private static final String USERID = "12345";
    private static LocalDateTime DATE;
    private static final String COMMENT = "versionComment";
    private static final String FILESTRING = "someStringForXML";
    private static final SaveTypeEnum saveType = SaveTypeEnum.AUTOSAVE;

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void createOrUpdateVersion() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, "artifactName", "DIAGRAMDESC", LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(artifact);
        when(this.verifyRelationService.checkIfVersionIsInitialVersion(any())).thenReturn(true);

        final ArtifactVersionUpload artifactVersionUploadTO = VersionBuilder.buildVersionUpload(COMMENT, FILESTRING, saveType);

        this.artifactVersionFacade.createVersion(artifactId, artifactVersionUploadTO);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.MEMBER);
    }

    @Test
    public void getAllVersion() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, "artifactName", "DIAGRAMDESC", LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(artifact);

        this.artifactVersionFacade.getAllVersions(artifactId);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.artifactVersionService, times(1)).getAllVersions(artifactId);
    }

    @Test
    public void getLatestVersion() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, "artifactName", "DIAGRAMDESC", LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(artifact);

        this.artifactVersionFacade.getLatestVersion(artifactId);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.artifactVersionService, times(1)).getLatestVersion(artifactId);
    }

    @Test
    public void getSingleVersion() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, "artifactName", "DIAGRAMDESC", LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(artifact);

        this.artifactVersionFacade.getVersion(artifactId, VERSIONID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.artifactVersionService, times(1)).getVersion(VERSIONID);
    }

}
