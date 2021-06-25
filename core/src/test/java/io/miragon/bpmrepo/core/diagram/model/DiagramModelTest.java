package io.miragon.bpmrepo.core.diagram.model;

import io.miragon.bpmrepo.core.diagram.DiagramBuilder;
import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramUpdate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class DiagramModelTest {

    private static final String REPOID = "42";
    private static final String DIAGRAMID = "001";
    private static final String DIAGRAMNAME = "diagram name";
    private static final String NEWDIAGRAMNAME = "new name";
    private static final String DIAGRAMDESC = "diagram description";
    private static final String NEWDIAGRAMDESC = "new description";
    private static LocalDateTime DATE;

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void updateDiagram() {
        final DiagramUpdate diagramTO = DiagramBuilder.buildDiagramUpdate(NEWDIAGRAMNAME, NEWDIAGRAMDESC);
        final Diagram bpmnDiagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);

        //update
        bpmnDiagram.updateDiagram(diagramTO);
        assertEquals(NEWDIAGRAMNAME, bpmnDiagram.getName());
        assertEquals(NEWDIAGRAMDESC, bpmnDiagram.getDescription());
        assertNotEquals(DATE, bpmnDiagram.getUpdatedDate());
    }

}
