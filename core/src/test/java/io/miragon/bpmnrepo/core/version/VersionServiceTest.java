package io.miragon.bpmnrepo.core.version;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionTO;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramVersionService;
import io.miragon.bpmnrepo.core.diagram.domain.mapper.VersionMapper;
import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagramVersion;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramVersionEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramVersionJpa;
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
public class VersionServiceTest {

    @InjectMocks
    private BpmnDiagramVersionService bpmnDiagramVersionService;

    @Mock
    private VersionMapper mapper;

    @Mock
    private BpmnDiagramVersionJpa bpmnDiagramVersionJpa;

    private static final String VERSIONID = "v01";
    private static final String DIAGRAMID = "123456";
    private static final String REPOID = "01";
    private static final String COMMENT = "VersionComment";
    private static final String FILESTRING = "somexmlString";
    private static final Integer RELEASE = 1;
    private static final Integer MILESTONE = 0;
    private static final SaveTypeEnum SAVETYPE = SaveTypeEnum.AUTOSAVE;

    private static LocalDateTime DATE;

    @BeforeAll
    public static void init(){
        DATE = LocalDateTime.now();
    }


    @Test
    public void updateVersion(){
        BpmnDiagramVersionEntity bpmnDiagramVersionEntity = VersionBuilder.buildVersionEntity(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);
        BpmnDiagramVersion bpmnDiagramVersion = VersionBuilder.buildVersion(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);
        BpmnDiagramVersionTO bpmnDiagramVersionTO = VersionBuilder.buildVersionTO(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);


        final ArgumentCaptor<BpmnDiagramVersionEntity> captor = ArgumentCaptor.forClass(BpmnDiagramVersionEntity.class);
        when(bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(DIAGRAMID)).thenReturn(bpmnDiagramVersionEntity);
        when(mapper.toModel(bpmnDiagramVersionEntity)).thenReturn(bpmnDiagramVersion);
        when(mapper.toEntity(bpmnDiagramVersion)).thenReturn(bpmnDiagramVersionEntity);
        when(bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdAndBpmnRepositoryIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(DIAGRAMID, REPOID)).thenReturn(bpmnDiagramVersionEntity);


        bpmnDiagramVersionService.updateVersion(bpmnDiagramVersionTO);
        verify(bpmnDiagramVersionJpa, times(1)).findFirstByBpmnDiagramIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(DIAGRAMID);
        verify(mapper, times(1)).toModel(bpmnDiagramVersionEntity);
        verify(bpmnDiagramVersionJpa, times(1)).save(captor.capture());
        verify(bpmnDiagramVersionJpa, times(1)).findFirstByBpmnDiagramIdAndBpmnRepositoryIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(DIAGRAMID, REPOID);

        final BpmnDiagramVersionEntity savedVersionEntity = captor.getValue();
        assertNotNull(savedVersionEntity);
        assertEquals(savedVersionEntity.getBpmnDiagramVersionFile(), FILESTRING);
        assertEquals(savedVersionEntity.getBpmnDiagramVersionRelease(), RELEASE);
    }

    @Test
    public void createInitialVersion(){
        BpmnDiagramVersionEntity bpmnDiagramVersionEntity = VersionBuilder.buildVersionEntity(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);
        BpmnDiagramVersion bpmnDiagramVersion = VersionBuilder.buildVersion(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);
        BpmnDiagramVersionTO bpmnDiagramVersionTO = VersionBuilder.buildVersionTO(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);

        when(mapper.toEntity(bpmnDiagramVersion)).thenReturn(bpmnDiagramVersionEntity);
        when(bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdAndBpmnRepositoryIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(DIAGRAMID, REPOID)).thenReturn(bpmnDiagramVersionEntity);

        bpmnDiagramVersionService.createInitialVersion(bpmnDiagramVersionTO);
        //verify(mapper, times(1)).toEntity(bpmnDiagramVersion);
        //verify(bpmnDiagramVersionJpa, times(1)).save(bpmnDiagramVersionEntity);

    }


}
