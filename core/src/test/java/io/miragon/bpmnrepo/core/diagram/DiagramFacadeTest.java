package io.miragon.bpmnrepo.core.diagram;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramVersionService;
import io.miragon.bpmnrepo.core.diagram.domain.business.VerifyRelationService;
import io.miragon.bpmnrepo.core.diagram.domain.facade.BpmnDiagramFacade;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpaRepository;
import io.miragon.bpmnrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmnrepo.core.repository.domain.business.BpmnRepositoryService;
import io.miragon.bpmnrepo.core.shared.enums.RoleEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

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
    private BpmnDiagramJpaRepository bpmnDiagramJpa;
    @Mock
    private BpmnRepositoryService bpmnRepositoryService;

    private static final String DIAGRAMID = "123456";
    private static final String REPOID = "01";
    private static final String DIAGRAMNAME = "TestDiagram";
    private static final String DIAGRAMDESC = "SomeDescription";
    private static final Integer EXISTINGDIAGRAMS = 5;
    private static LocalDateTime DATE;

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void createOrUpdateDiagram() {
        final BpmnDiagramUploadTO bpmnDiagramUploadTO = DiagramBuilder.buildUploadTO(DIAGRAMID, DIAGRAMNAME, DIAGRAMDESC);

        this.bpmnDiagramFacade.createOrUpdateDiagram(REPOID, bpmnDiagramUploadTO);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.MEMBER);

    }

    @Test
    public void getSingleDiagram() {
        this.bpmnDiagramFacade.getSingleDiagram(REPOID, DIAGRAMID);
        verify(this.verifyRelationService, times(1)).verifyDiagramIsInSpecifiedRepository(REPOID, DIAGRAMID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.bpmnDiagramService, times(1)).getSingleDiagram(DIAGRAMID);
    }

    @Test
    public void deleteDiagram() {
        when(this.bpmnDiagramService.countExistingDiagrams(REPOID)).thenReturn(EXISTINGDIAGRAMS);

        this.bpmnDiagramFacade.deleteDiagram(REPOID, DIAGRAMID);

        verify(this.verifyRelationService, times(1)).verifyDiagramIsInSpecifiedRepository(REPOID, DIAGRAMID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.ADMIN);
        verify(this.bpmnDiagramVersionService, times(1)).deleteAllByDiagramId(DIAGRAMID);
        verify(this.bpmnDiagramService, times(1)).deleteDiagram(DIAGRAMID);
        verify(this.bpmnRepositoryService, times(1)).updateExistingDiagrams(REPOID, EXISTINGDIAGRAMS);
    }
}
