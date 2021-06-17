package io.miragon.bpmnrepo.core.version;

import io.miragon.bpmnrepo.core.diagram.domain.facade.BpmnDiagramVersionFacade;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramVersionService;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmnrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmnrepo.core.diagram.domain.business.VerifyRelationService;
import io.miragon.bpmnrepo.core.shared.enums.RoleEnum;
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
    private BpmnDiagramVersionFacade bpmnDiagramVersionFacade;

    @Mock
    private AuthService authService;

    @Mock
    private VerifyRelationService verifyRelationService;

    @Mock
    private BpmnDiagramVersionService bpmnDiagramVersionService;

    @Mock
    private BpmnDiagramService bpmnDiagramService;


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
    public static void init(){
        DATE = LocalDateTime.now();
    }


    @Test
    public void createOrUpdateVersion(){
        BpmnDiagramVersionUploadTO bpmnDiagramVersionUploadTO = VersionBuilder.buildVersionUploadTO(COMMENT, FILESTRING, saveType);

        this.bpmnDiagramVersionFacade.createOrUpdateVersion(REPOID, DIAGRAMID, bpmnDiagramVersionUploadTO);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.MEMBER);
        verify(this.verifyRelationService, times(1)).verifyDiagramIsInSpecifiedRepository(REPOID, DIAGRAMID);
    }

    @Test
    public void getAllVersion(){

        this.bpmnDiagramVersionFacade.getAllVersions(REPOID, DIAGRAMID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.verifyRelationService, times(1)).verifyDiagramIsInSpecifiedRepository(REPOID, DIAGRAMID);
        verify(this.bpmnDiagramVersionService, times(1)).getAllVersions(DIAGRAMID);
    }

    @Test
    public void getLatestVersion(){

        this.bpmnDiagramVersionFacade.getLatestVersion(REPOID, DIAGRAMID);
        verify(this.verifyRelationService, times(1)).verifyDiagramIsInSpecifiedRepository(REPOID, DIAGRAMID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.bpmnDiagramVersionService, times(1)).getLatestVersion(DIAGRAMID);
    }

    @Test
    public void getSingleVersion(){
        this.bpmnDiagramVersionFacade.getSingleVersion(REPOID, DIAGRAMID, VERSIONID);
        verify(this.verifyRelationService, times(1)).verifyVersionIsFromSpecifiedDiagram(DIAGRAMID, VERSIONID);
        verify(this.verifyRelationService, times(1)).verifyDiagramIsInSpecifiedRepository(REPOID, DIAGRAMID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.bpmnDiagramVersionService, times(1)).getSingleVersion(VERSIONID);
    }

}
