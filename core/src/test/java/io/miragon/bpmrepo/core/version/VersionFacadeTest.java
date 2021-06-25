package io.miragon.bpmrepo.core.version;

import io.miragon.bpmrepo.core.diagram.DiagramBuilder;
import io.miragon.bpmrepo.core.diagram.domain.business.DiagramService;
import io.miragon.bpmrepo.core.diagram.domain.business.DiagramVersionService;
import io.miragon.bpmrepo.core.diagram.domain.business.VerifyRelationService;
import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.diagram.domain.facade.DiagramVersionFacade;
import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramVersionUpload;
import io.miragon.bpmrepo.core.repository.domain.business.AuthService;
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
    private DiagramVersionFacade diagramVersionFacade;

    @Mock
    private AuthService authService;

    @Mock
    private VerifyRelationService verifyRelationService;

    @Mock
    private DiagramVersionService diagramVersionService;

    @Mock
    private DiagramService diagramService;

    private static final String REPOID = "42";
    private static final String DIAGRAMID = "001";
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
        final Diagram diagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, "DIAGRAMNAME", "DIAGRAMDESC", LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.diagramService.getDiagramById(DIAGRAMID)).thenReturn(diagram);
        when(this.verifyRelationService.checkIfVersionIsInitialVersion(any())).thenReturn(true);

        final DiagramVersionUpload bpmnDiagramVersionUploadTO = VersionBuilder.buildVersionUpload(COMMENT, FILESTRING, saveType);

        this.diagramVersionFacade.createOrUpdateVersion(DIAGRAMID, bpmnDiagramVersionUploadTO);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.MEMBER);
    }

    @Test
    public void getAllVersion() {
        final Diagram diagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, "DIAGRAMNAME", "DIAGRAMDESC", LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.diagramService.getDiagramById(DIAGRAMID)).thenReturn(diagram);

        this.diagramVersionFacade.getAllVersions(DIAGRAMID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.diagramVersionService, times(1)).getAllVersions(DIAGRAMID);
    }

    @Test
    public void getLatestVersion() {
        final Diagram diagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, "DIAGRAMNAME", "DIAGRAMDESC", LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.diagramService.getDiagramById(DIAGRAMID)).thenReturn(diagram);

        this.diagramVersionFacade.getLatestVersion(DIAGRAMID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.diagramVersionService, times(1)).getLatestVersion(DIAGRAMID);
    }

    @Test
    public void getSingleVersion() {
        final Diagram diagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, "DIAGRAMNAME", "DIAGRAMDESC", LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.diagramService.getDiagramById(DIAGRAMID)).thenReturn(diagram);

        this.diagramVersionFacade.getVersion(DIAGRAMID, VERSIONID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.diagramVersionService, times(1)).getVersion(VERSIONID);
    }

}
