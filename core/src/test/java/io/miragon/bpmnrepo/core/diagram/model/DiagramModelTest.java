package io.miragon.bpmnrepo.core.diagram.model;

import io.miragon.bpmnrepo.core.diagram.DiagramBuilder;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagram;
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
    private static final String REPONAME = "repo name";
    private static final String DIAGRAMNAME = "diagram name";
    private static final String NEWDIAGRAMNAME = "new name";
    private static final String REPODESC = "repository description";
    private static final String DIAGRAMDESC = "diagram description";
    private static final String NEWDIAGRAMDESC = "new description";
    private static LocalDateTime DATE;


    @BeforeAll
    public static void init(){
        DATE = LocalDateTime.now();
    }

    @Test
    public void updateDiagram(){
        BpmnDiagramUploadTO bpmnDiagramUploadTOName = DiagramBuilder.buildUploadTO(DIAGRAMID, NEWDIAGRAMNAME, null);
        BpmnDiagramTO bpmnDiagramTO = new BpmnDiagramTO(REPOID, bpmnDiagramUploadTOName);
        BpmnDiagram bpmnDiagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);

        //update name only
        bpmnDiagram.updateDiagram(bpmnDiagramTO);
        assertEquals(NEWDIAGRAMNAME, bpmnDiagram.getBpmnDiagramName());
        assertEquals(DIAGRAMDESC, bpmnDiagram.getBpmnDiagramDescription());
        assertNotEquals(DATE, bpmnDiagram.getUpdatedDate());
        //update desc only
        bpmnDiagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        BpmnDiagramUploadTO bpmnDiagramUploadTODesc = DiagramBuilder.buildUploadTO(DIAGRAMID, null, NEWDIAGRAMDESC);
        bpmnDiagramTO = new BpmnDiagramTO(REPOID, bpmnDiagramUploadTODesc);
        bpmnDiagram.updateDiagram(bpmnDiagramTO);
        assertEquals(NEWDIAGRAMDESC, bpmnDiagram.getBpmnDiagramDescription());
        assertEquals(DIAGRAMNAME, bpmnDiagram.getBpmnDiagramName());
        assertNotEquals(DATE, bpmnDiagram.getUpdatedDate());

        //update both
        bpmnDiagram = DiagramBuilder.buildDiagram(DIAGRAMID, REPOID, DIAGRAMNAME, DIAGRAMDESC, DATE, DATE);
        BpmnDiagramUploadTO bpmnDiagramUploadTOBoth = DiagramBuilder.buildUploadTO(DIAGRAMID, NEWDIAGRAMNAME, NEWDIAGRAMDESC);
        bpmnDiagramTO = new BpmnDiagramTO(REPOID, bpmnDiagramUploadTOBoth);
        bpmnDiagram.updateDiagram(bpmnDiagramTO);
        assertEquals(NEWDIAGRAMNAME, bpmnDiagram.getBpmnDiagramName());
        assertEquals(NEWDIAGRAMDESC, bpmnDiagram.getBpmnDiagramDescription());
        assertNotEquals(DATE, bpmnDiagram.getUpdatedDate());

    }


}
