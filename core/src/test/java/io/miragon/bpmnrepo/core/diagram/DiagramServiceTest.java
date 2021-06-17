package io.miragon.bpmnrepo.core.diagram;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.mapper.DiagramMapper;
import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagram;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpa;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DiagramServiceTest {

    @InjectMocks
    private BpmnDiagramService bpmnDiagramService;

    @Mock
    private BpmnDiagramJpa bpmnDiagramJpa;

    @Mock
    private DiagramMapper mapper;

    private static final String DIAGRAMID = "123456";
    private static final String REPOID = "01";
    private static final String DIAGRAMNAME = "TestDiagram";
    private static final String DIAGRAMDESC = "SomeDescription";
    private static LocalDateTime DATE;

    @BeforeAll
    public static void init(){
        DATE = LocalDateTime.now();
    }



    @Test
    public void createDiagram(){
        BpmnDiagram bpmnDiagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        BpmnDiagramTO bpmnDiagramTO = DiagramBuilder.buildDiagramTO(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC);
        BpmnDiagramEntity bpmnDiagramEntity = DiagramBuilder.buildDiagramEntity(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);

        final ArgumentCaptor<BpmnDiagram> captor = ArgumentCaptor.forClass(BpmnDiagram.class);
        when(mapper.toModel(any(BpmnDiagramTO.class))).thenReturn(bpmnDiagram);
        when(mapper.toEntity(bpmnDiagram)).thenReturn(bpmnDiagramEntity);

        bpmnDiagramService.createDiagram(bpmnDiagramTO);
        verify(mapper, times(1)).toModel(bpmnDiagramTO);
        verify(mapper, times(1)).toEntity(captor.capture());
        verify(bpmnDiagramJpa, times(1)).save(bpmnDiagramEntity);

        final BpmnDiagram savedDiagram = captor.getValue();
        assertNotNull(savedDiagram);
        assertEquals(savedDiagram.getUpdatedDate(), DATE);
        assertEquals(savedDiagram.getCreatedDate(), DATE);
    }


    @Test
    public void updateDiagram(){
        BpmnDiagramTO bpmnDiagramTO = DiagramBuilder.buildDiagramTO(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC);
        BpmnDiagramEntity bpmnDiagramEntity = DiagramBuilder.buildDiagramEntity(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        BpmnDiagram bpmnDiagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);

        final ArgumentCaptor<BpmnDiagramEntity> captor = ArgumentCaptor.forClass(BpmnDiagramEntity.class);
        when(bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(any())).thenReturn(bpmnDiagramEntity);
        when(mapper.toModel(bpmnDiagramEntity)).thenReturn(bpmnDiagram);
        when(mapper.toEntity(bpmnDiagram)).thenReturn(bpmnDiagramEntity);

        bpmnDiagramService.updateDiagram(bpmnDiagramTO);
        verify(bpmnDiagramJpa, times(1)).findBpmnDiagramEntityByBpmnDiagramIdEquals(DIAGRAMID);
        verify(bpmnDiagramJpa, times(1)).save(captor.capture());

        final BpmnDiagramEntity savedDiagram = captor.getValue();
        assertEquals(REPOID, savedDiagram.getBpmnRepositoryId());
        assertEquals(DATE, savedDiagram.getUpdatedDate());

    }

    @Test
    public void getSingleDiagram(){
        BpmnDiagramEntity bpmnDiagramEntity = DiagramBuilder.buildDiagramEntity(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        BpmnDiagramTO bpmnDiagramTO = DiagramBuilder.buildDiagramTO(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC);

        when(bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(DIAGRAMID)).thenReturn(bpmnDiagramEntity);
        when(mapper.toTO(bpmnDiagramEntity)).thenReturn(bpmnDiagramTO);

        bpmnDiagramService.getSingleDiagram(DIAGRAMID);
        verify(bpmnDiagramJpa, times(1)).findBpmnDiagramEntityByBpmnDiagramIdEquals(DIAGRAMID);
        verify(mapper, times(1)).toTO(bpmnDiagramEntity);
    }

    @Test
    public void deleteDiagram(){
        when(bpmnDiagramJpa.deleteBpmnDiagramEntitiyByBpmnDiagramId(DIAGRAMID)).thenReturn(any(Integer.class));

        bpmnDiagramService.deleteDiagram(DIAGRAMID);
        verify(bpmnDiagramJpa, times(1)).deleteBpmnDiagramEntitiyByBpmnDiagramId(DIAGRAMID);
    }

    @Test
    public void deleteAllByDiagramId(){
        when(bpmnDiagramJpa.deleteAllByBpmnRepositoryId(REPOID)).thenReturn(any(Integer.class));
        bpmnDiagramService.deleteAllByRepositoryId(REPOID);
        verify(bpmnDiagramJpa, times(1)).deleteAllByBpmnRepositoryId(REPOID);

    }
}
