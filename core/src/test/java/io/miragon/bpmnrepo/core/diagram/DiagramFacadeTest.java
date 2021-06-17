package io.miragon.bpmnrepo.core.diagram;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramVersionService;
import io.miragon.bpmnrepo.core.diagram.domain.facade.BpmnDiagramFacade;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpa;
import io.miragon.bpmnrepo.core.repository.domain.business.BpmnRepositoryService;
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
public class DiagramFacadeTest {

    @InjectMocks
    private BpmnDiagramFacade bpmnDiagramFacade;

    @Mock
    private AuthService authService;
    @Mock
    private VerifyRelationService verifyRelationService;
    @Mock
    private BpmnDiagramService bpmnDiagramService;
    @Mock
    private BpmnDiagramVersionService bpmnDiagramVersionService;
    @Mock
    private BpmnDiagramJpa bpmnDiagramJpa;
    @Mock
    private BpmnRepositoryService bpmnRepositoryService;

    private static final String DIAGRAMID = "123456";
    private static final String REPOID = "01";
    private static final String DIAGRAMNAME = "TestDiagram";
    private static final String DIAGRAMDESC = "SomeDescription";
    private static final Integer EXISTINGDIAGRAMS = 5;
    private static LocalDateTime DATE;

    @BeforeAll
    public static void init(){
        DATE = LocalDateTime.now();
    }

    @Test
    public void createOrUpdateDiagram(){
        BpmnDiagramUploadTO bpmnDiagramUploadTO = DiagramBuilder.buildUploadTO(DIAGRAMID, DIAGRAMNAME, DIAGRAMDESC);

        bpmnDiagramFacade.createOrUpdateDiagram(REPOID, bpmnDiagramUploadTO);
        verify(authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.MEMBER);


    }

    @Test
    public void getSingleDiagram(){
        bpmnDiagramFacade.getSingleDiagram(REPOID, DIAGRAMID);
        verify(verifyRelationService, times(1)).verifyDiagramIsInSpecifiedRepository(REPOID, DIAGRAMID);
        verify(authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(bpmnDiagramService, times(1)).getSingleDiagram(DIAGRAMID);
    }

    @Test
    public void deleteDiagram(){
        when(bpmnDiagramService.countExistingDiagrams(REPOID)).thenReturn(EXISTINGDIAGRAMS);

        bpmnDiagramFacade.deleteDiagram(REPOID, DIAGRAMID);

        verify(verifyRelationService, times(1)).verifyDiagramIsInSpecifiedRepository(REPOID, DIAGRAMID);
        verify(authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.ADMIN);
        verify(bpmnDiagramVersionService, times(1)).deleteAllByDiagramId(DIAGRAMID);
        verify(bpmnDiagramService, times(1)).deleteDiagram(DIAGRAMID);
        verify(bpmnRepositoryService, times(1)).updateExistingDiagrams(REPOID, EXISTINGDIAGRAMS);
    }
}
