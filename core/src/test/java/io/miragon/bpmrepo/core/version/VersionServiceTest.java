package io.miragon.bpmrepo.core.version;

import io.miragon.bpmrepo.core.diagram.domain.business.DiagramVersionService;
import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.diagram.domain.mapper.VersionMapper;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramVersion;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.DiagramVersionEntity;
import io.miragon.bpmrepo.core.diagram.infrastructure.repository.DiagramVersionJpaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VersionServiceTest {

    @InjectMocks
    private DiagramVersionService bpmnDiagramVersionService;

    @Mock
    private VersionMapper mapper;

    @Mock
    private DiagramVersionJpaRepository diagramVersionJpaRepository;

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
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void updateVersion() {
        final DiagramVersionEntity diagramVersionEntity = VersionBuilder
                .buildVersionEntity(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);
        final DiagramVersion diagramVersion = VersionBuilder
                .buildVersion(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);
        final DiagramVersion diagramVersionUpdate = VersionBuilder
                .buildVersion(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);

        final ArgumentCaptor<DiagramVersionEntity> captor = ArgumentCaptor.forClass(DiagramVersionEntity.class);
        when(this.diagramVersionJpaRepository.findFirstByDiagramIdOrderByReleaseDescMilestoneDesc(DIAGRAMID))
                .thenReturn(Optional.of(diagramVersionEntity));
        when(this.mapper.mapToModel(diagramVersionEntity)).thenReturn(diagramVersion);
        when(this.mapper.mapToEntity(diagramVersion)).thenReturn(diagramVersionEntity);
        when(this.diagramVersionJpaRepository
                .findFirstByDiagramIdAndRepositoryIdOrderByReleaseDescMilestoneDesc(DIAGRAMID, REPOID))
                .thenReturn(diagramVersionEntity);
        when(this.diagramVersionJpaRepository.save(any())).thenReturn(diagramVersionEntity);

        this.bpmnDiagramVersionService.updateVersion(diagramVersionUpdate);
        verify(this.diagramVersionJpaRepository, times(1)).findFirstByDiagramIdOrderByReleaseDescMilestoneDesc(DIAGRAMID);
        verify(this.mapper, times(1)).mapToModel(diagramVersionEntity);
        verify(this.diagramVersionJpaRepository, times(1)).save(captor.capture());
        verify(this.diagramVersionJpaRepository, times(1))
                .findFirstByDiagramIdOrderByReleaseDescMilestoneDesc(DIAGRAMID);

        final DiagramVersionEntity savedVersionEntity = captor.getValue();
        assertNotNull(savedVersionEntity);
        assertEquals(savedVersionEntity.getXml(), FILESTRING);
        assertEquals(savedVersionEntity.getRelease(), RELEASE);
    }

    @Test
    @Disabled
    public void createInitialVersion() {
        final DiagramVersionEntity diagramVersionEntity = VersionBuilder
                .buildVersionEntity(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);
        final DiagramVersion diagramVersion = VersionBuilder
                .buildVersion(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);
        final DiagramVersion diagramVersionCreation = VersionBuilder
                .buildVersion(VERSIONID, DIAGRAMID, REPOID, COMMENT, RELEASE, MILESTONE, FILESTRING, SAVETYPE);

        when(this.mapper.mapToEntity(diagramVersion)).thenReturn(diagramVersionEntity);
        when(this.diagramVersionJpaRepository
                .findFirstByDiagramIdAndRepositoryIdOrderByReleaseDescMilestoneDesc(DIAGRAMID, REPOID))
                .thenReturn(diagramVersionEntity);

        this.bpmnDiagramVersionService.createInitialVersion(diagramVersionCreation);
        //verify(mapper, times(1)).toEntity(bpmnDiagramVersion);
        //verify(bpmnDiagramVersionJpa, times(1)).save(bpmnDiagramVersionEntity);

    }

}
