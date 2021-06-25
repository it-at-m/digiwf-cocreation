package io.miragon.bpmrepo.core.diagram;

import io.miragon.bpmrepo.core.diagram.domain.business.DiagramService;
import io.miragon.bpmrepo.core.diagram.domain.business.DiagramVersionService;
import io.miragon.bpmrepo.core.diagram.domain.facade.DiagramFacade;
import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmrepo.core.repository.domain.business.RepositoryService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
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
    private DiagramFacade diagramFacade;

    @Mock
    private AuthService authService;

    @Mock
    private DiagramService diagramService;
    @Mock
    private DiagramVersionService diagramVersionService;
    @Mock
    private RepositoryService repositoryService;

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
        final Diagram diagram = DiagramBuilder.buildDiagram(DIAGRAMID, DIAGRAMNAME, DIAGRAMDESC);

        this.diagramFacade.createDiagram(REPOID, diagram);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.MEMBER);

    }

    @Test
    public void getSingleDiagram() {
        final Diagram diagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, LocalDateTime.now(), LocalDateTime.now());
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.diagramService.getDiagramById(DIAGRAMID)).thenReturn(diagram);

        this.diagramFacade.getDiagram(DIAGRAMID);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.VIEWER);
        verify(this.diagramService, times(1)).getDiagramById(DIAGRAMID);
    }

    @Test
    public void deleteDiagram() {
        final Diagram diagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, LocalDateTime.now(), LocalDateTime.now());

        when(this.diagramService.countExistingDiagrams(REPOID)).thenReturn(EXISTINGDIAGRAMS);
        doNothing().when(this.authService).checkIfOperationIsAllowed(any(), any());
        when(this.diagramService.getDiagramById(DIAGRAMID)).thenReturn(diagram);

        this.diagramFacade.deleteDiagram(DIAGRAMID);

        verify(this.authService, times(1)).checkIfOperationIsAllowed(REPOID, RoleEnum.ADMIN);
        verify(this.diagramVersionService, times(1)).deleteAllByDiagramId(DIAGRAMID);
        verify(this.diagramService, times(1)).deleteDiagram(DIAGRAMID);
        verify(this.repositoryService, times(1)).updateExistingDiagrams(REPOID, EXISTINGDIAGRAMS);
    }
}
