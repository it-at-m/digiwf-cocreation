package io.miragon.bpmrepo.core.version;

import io.miragon.bpmrepo.core.artifact.ArtifactBuilder;
import io.miragon.bpmrepo.core.artifact.domain.facade.ArtifactMilestoneFacade;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestoneUpload;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactMilestoneService;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.LockService;
import io.miragon.bpmrepo.core.artifact.domain.service.VerifyRelationService;
import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Disabled
public class MilestoneFacadeTest {

    @InjectMocks
    private ArtifactMilestoneFacade artifactMilestoneFacade;

    @Mock
    private AuthService authService;

    @Mock
    private VerifyRelationService verifyRelationService;

    @Mock
    private ArtifactMilestoneService artifactMilestoneService;

    @Mock
    private ArtifactService artifactService;

    @Mock
    private LockService lockService;

    private static final String REPOID = "42";
    private static final String artifactId = "001";
    private static final String VERSIONID = "v-01";
    private static final String REPONAME = "repo name";
    private static final String REPODESC = "repository description";
    private static final String ARTIFACTNAME = "NAME";
    private static final String ARTIFACTDESC = "DESC";
    private static final String USERID = "12345";
    private static LocalDateTime DATE;
    private static final String COMMENT = "versionComment";
    private static final String FILESTRING = "someStringForXML";
    private static final String FILE_TYPE = "BPMN";

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void createOrUpdateMilestone() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, ARTIFACTNAME, ARTIFACTDESC, FILE_TYPE, LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(Optional.of(artifact));
        when(this.verifyRelationService.checkIfMilestoneIsInitialMilestone(any())).thenReturn(true);

        final ArtifactMilestoneUpload artifactMilestoneUploadTO = MilestoneBuilder.buildMilestoneUpload(COMMENT, FILESTRING);

        this.artifactMilestoneFacade.createMilestone(artifactId, artifactMilestoneUploadTO);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.MEMBER);
    }

    @Test
    public void getAllMilestone() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, ARTIFACTNAME, ARTIFACTDESC, FILE_TYPE, LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(Optional.of(artifact));

        this.artifactMilestoneFacade.getAllMilestones(artifactId);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.artifactMilestoneService, times(1)).getAllMilestones(artifactId);
    }

    @Test
    public void getLatestMilestone() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, ARTIFACTNAME, ARTIFACTDESC, FILE_TYPE, LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(Optional.of(artifact));

        this.artifactMilestoneFacade.getLatestMilestone(artifactId);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.artifactMilestoneService, times(1)).getLatestMilestone(artifactId);
    }

    @Test
    public void getSingleMilestone() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, ARTIFACTNAME, ARTIFACTDESC, FILE_TYPE, LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.artifactService.getArtifactById(artifactId)).thenReturn(Optional.of(artifact));

        this.artifactMilestoneFacade.getMilestone(artifactId, VERSIONID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.artifactMilestoneService, times(1)).getMilestone(VERSIONID);
    }

}
