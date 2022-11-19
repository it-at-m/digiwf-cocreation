package io.miragon.bpmrepo.core.diagram;

import io.miragon.bpmrepo.core.diagram.domain.business.DiagramService;
import io.miragon.bpmrepo.core.diagram.domain.mapper.DiagramMapper;
import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramUpdate;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.DiagramEntity;
import io.miragon.bpmrepo.core.diagram.infrastructure.repository.DiagramJpaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DiagramServiceTest {

    @InjectMocks
    private DiagramService diagramService;

    @Mock
    private DiagramJpaRepository diagramJpaRepository;

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
        final Diagram diagram = DiagramBuilder
                .buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        final DiagramEntity diagramEntity = DiagramBuilder.buildDiagramEntity(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);

        final ArgumentCaptor<Diagram> captor = ArgumentCaptor.forClass(Diagram.class);
        when(this.mapper.mapToModel(any(DiagramEntity.class))).thenReturn(diagram);
        when(this.mapper.mapToEntity(diagram)).thenReturn(diagramEntity);
        when(this.diagramJpaRepository.save(any())).thenReturn(diagramEntity);

        this.diagramService.createDiagram(diagram);
        verify(this.mapper, times(1)).mapToModel(diagramEntity);
        verify(this.mapper, times(1)).mapToEntity(captor.capture());
        verify(this.diagramJpaRepository, times(1)).save(diagramEntity);

        final io.miragon.bpmrepo.core.diagram.domain.model.Diagram savedDiagram = captor.getValue();
        assertNotNull(savedDiagram);
        assertEquals(savedDiagram.getUpdatedDate(), DATE);
        assertEquals(savedDiagram.getCreatedDate(), DATE);
    }

    @Test
    public void updateDiagram() {
        final DiagramUpdate diagramUpdate = DiagramBuilder.buildDiagramUpdate(DIAGRAMNAME, DIAGRAMDESC);
        final DiagramEntity diagramEntity = DiagramBuilder.buildDiagramEntity(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        final Diagram diagram = DiagramBuilder
                .buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);

        final ArgumentCaptor<DiagramEntity> captor = ArgumentCaptor.forClass(DiagramEntity.class);
        when(this.diagramJpaRepository.findById(any())).thenReturn(Optional.of(diagramEntity));
        when(this.mapper.mapToModel(diagramEntity)).thenReturn(diagram);
        when(this.mapper.mapToEntity(diagram)).thenReturn(diagramEntity);

        this.diagramService.updateDiagram(DIAGRAMID, diagramUpdate);
        verify(this.diagramJpaRepository, times(1)).findById(DIAGRAMID);
        verify(this.diagramJpaRepository, times(1)).save(captor.capture());

        final DiagramEntity savedDiagram = captor.getValue();
        assertEquals(REPOID, savedDiagram.getRepositoryId());
        assertEquals(DATE, savedDiagram.getUpdatedDate());

    }

    @Test
    public void getSingleDiagram() {
        final DiagramEntity diagramEntity = DiagramBuilder.buildDiagramEntity(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        final Diagram diagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, LocalDateTime.now(), LocalDateTime.now());

        when(this.diagramJpaRepository.findById(DIAGRAMID)).thenReturn(Optional.of(diagramEntity));
        when(this.mapper.mapToModel(diagramEntity)).thenReturn(diagram);

        this.diagramService.getDiagramById(DIAGRAMID);
        verify(this.diagramJpaRepository, times(1)).findById(DIAGRAMID);
        verify(this.mapper, times(1)).mapToModel(diagramEntity);
    }

    @Test
    public void deleteDiagram() {
        doNothing().when(this.diagramJpaRepository).deleteById(DIAGRAMID);

        this.diagramService.deleteDiagram(DIAGRAMID);
        verify(this.diagramJpaRepository, times(1)).deleteById(DIAGRAMID);
    }

    @Test
    public void deleteAllByDiagramId() {
        when(this.diagramJpaRepository.deleteAllByRepositoryId(REPOID)).thenReturn(any(Integer.class));
        this.diagramService.deleteAllByRepositoryId(REPOID);
        verify(this.diagramJpaRepository, times(1)).deleteAllByRepositoryId(REPOID);

    }
}
