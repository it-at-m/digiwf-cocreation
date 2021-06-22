package io.miragon.bpmnrepo.core.diagram;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.mapper.DiagramMapper;
import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagram;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DiagramServiceTest {

    @InjectMocks
    private BpmnDiagramService bpmnDiagramService;

    @Mock
    private BpmnDiagramJpaRepository bpmnDiagramJpa;

    @Mock
    private DiagramMapper mapper;

    private static final String DIAGRAMID = "123456";
    private static final String REPOID = "01";
    private static final String DIAGRAMNAME = "TestDiagram";
    private static final String DIAGRAMDESC = "SomeDescription";
    private static LocalDateTime DATE;

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void createDiagram() {
        final BpmnDiagram bpmnDiagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        final BpmnDiagramTO bpmnDiagramTO = DiagramBuilder.buildDiagramTO(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC);
        final BpmnDiagramEntity bpmnDiagramEntity = DiagramBuilder.buildDiagramEntity(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);

        final ArgumentCaptor<BpmnDiagram> captor = ArgumentCaptor.forClass(BpmnDiagram.class);
        when(this.mapper.toModel(any(BpmnDiagramTO.class))).thenReturn(bpmnDiagram);
        when(this.mapper.toEntity(bpmnDiagram)).thenReturn(bpmnDiagramEntity);

        this.bpmnDiagramService.createDiagram(bpmnDiagramTO);
        verify(this.mapper, times(1)).toModel(bpmnDiagramTO);
        verify(this.mapper, times(1)).toEntity(captor.capture());
        verify(this.bpmnDiagramJpa, times(1)).save(bpmnDiagramEntity);

        final BpmnDiagram savedDiagram = captor.getValue();
        assertNotNull(savedDiagram);
        assertEquals(savedDiagram.getUpdatedDate(), DATE);
        assertEquals(savedDiagram.getCreatedDate(), DATE);
    }

    @Test
    public void updateDiagram() {
        final BpmnDiagramTO bpmnDiagramTO = DiagramBuilder.buildDiagramTO(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC);
        final BpmnDiagramEntity bpmnDiagramEntity = DiagramBuilder.buildDiagramEntity(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        final BpmnDiagram bpmnDiagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);

        final ArgumentCaptor<BpmnDiagramEntity> captor = ArgumentCaptor.forClass(BpmnDiagramEntity.class);
        when(this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(any())).thenReturn(bpmnDiagramEntity);
        when(this.mapper.toModel(bpmnDiagramEntity)).thenReturn(bpmnDiagram);
        when(this.mapper.toEntity(bpmnDiagram)).thenReturn(bpmnDiagramEntity);

        this.bpmnDiagramService.updateDiagram(bpmnDiagramTO);
        verify(this.bpmnDiagramJpa, times(1)).findBpmnDiagramEntityByBpmnDiagramIdEquals(DIAGRAMID);
        verify(this.bpmnDiagramJpa, times(1)).save(captor.capture());

        final BpmnDiagramEntity savedDiagram = captor.getValue();
        assertEquals(REPOID, savedDiagram.getBpmnRepositoryId());
        assertEquals(DATE, savedDiagram.getUpdatedDate());

    }

    @Test
    public void getSingleDiagram() {
        final BpmnDiagramEntity bpmnDiagramEntity = DiagramBuilder.buildDiagramEntity(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        final BpmnDiagramTO bpmnDiagramTO = DiagramBuilder.buildDiagramTO(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC);

        when(this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(DIAGRAMID)).thenReturn(bpmnDiagramEntity);
        when(this.mapper.toTO(bpmnDiagramEntity)).thenReturn(bpmnDiagramTO);

        this.bpmnDiagramService.getSingleDiagram(DIAGRAMID);
        verify(this.bpmnDiagramJpa, times(1)).findBpmnDiagramEntityByBpmnDiagramIdEquals(DIAGRAMID);
        verify(this.mapper, times(1)).toTO(bpmnDiagramEntity);
    }

    @Test
    public void deleteDiagram() {
        when(this.bpmnDiagramJpa.deleteBpmnDiagramEntityByBpmnDiagramId(DIAGRAMID)).thenReturn(any(Integer.class));

        this.bpmnDiagramService.deleteDiagram(DIAGRAMID);
        verify(this.bpmnDiagramJpa, times(1)).deleteBpmnDiagramEntityByBpmnDiagramId(DIAGRAMID);
    }

    @Test
    public void deleteAllByDiagramId() {
        when(this.bpmnDiagramJpa.deleteAllByBpmnRepositoryId(REPOID)).thenReturn(any(Integer.class));
        this.bpmnDiagramService.deleteAllByRepositoryId(REPOID);
        verify(this.bpmnDiagramJpa, times(1)).deleteAllByBpmnRepositoryId(REPOID);

    }
}
