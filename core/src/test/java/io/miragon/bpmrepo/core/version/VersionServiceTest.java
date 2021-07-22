package io.miragon.bpmrepo.core.version;

import io.miragon.bpmrepo.core.artifact.domain.business.ArtifactVersionService;
import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.mapper.VersionMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactVersionEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.ArtifactVersionJpaRepository;
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
    private ArtifactVersionService bpmnArtifactVersionService;

    @Mock
    private VersionMapper mapper;

    @Mock
    private ArtifactVersionJpaRepository artifactVersionJpaRepository;

    private static final String VERSIONID = "v01";
    private static final String artifactId = "123456";
    private static final String REPOID = "01";
    private static final String COMMENT = "VersionComment";
    private static final String FILESTRING = "somexmlString";
    private static final Integer MILESTONE = 1;
    private static final SaveTypeEnum SAVETYPE = SaveTypeEnum.AUTOSAVE;

    private static LocalDateTime DATE;

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void updateVersion() {
        final ArtifactVersionEntity artifactVersionEntity = VersionBuilder
                .buildVersionEntity(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);
        final ArtifactVersion artifactVersion = VersionBuilder
                .buildVersion(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);
        final ArtifactVersion artifactVersionUpdate = VersionBuilder
                .buildVersion(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);

        final ArgumentCaptor<ArtifactVersionEntity> captor = ArgumentCaptor.forClass(ArtifactVersionEntity.class);
        when(this.artifactVersionJpaRepository.findFirstByArtifactIdOrderByMilestoneDesc(artifactId))
                .thenReturn(Optional.of(artifactVersionEntity));
        when(this.mapper.mapToModel(artifactVersionEntity)).thenReturn(artifactVersion);
        when(this.mapper.mapToEntity(artifactVersion)).thenReturn(artifactVersionEntity);
        when(this.artifactVersionJpaRepository
                .findFirstByArtifactIdAndRepositoryIdOrderByMilestoneDesc(artifactId, REPOID))
                .thenReturn(artifactVersionEntity);
        when(this.artifactVersionJpaRepository.save(any())).thenReturn(artifactVersionEntity);

        this.bpmnArtifactVersionService.updateVersion(artifactVersionUpdate);
        verify(this.artifactVersionJpaRepository, times(1)).findFirstByArtifactIdOrderByMilestoneDesc(artifactId);
        verify(this.mapper, times(1)).mapToModel(artifactVersionEntity);
        verify(this.artifactVersionJpaRepository, times(1)).save(captor.capture());
        verify(this.artifactVersionJpaRepository, times(1))
                .findFirstByArtifactIdOrderByMilestoneDesc(artifactId);

        final ArtifactVersionEntity savedVersionEntity = captor.getValue();
        assertNotNull(savedVersionEntity);
        assertEquals(savedVersionEntity.getXml(), FILESTRING);
    }

    @Test
    @Disabled
    public void createInitialVersion() {
        final ArtifactVersionEntity artifactVersionEntity = VersionBuilder
                .buildVersionEntity(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);
        final ArtifactVersion artifactVersion = VersionBuilder
                .buildVersion(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);
        final ArtifactVersion artifactVersionCreation = VersionBuilder
                .buildVersion(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);

        when(this.mapper.mapToEntity(artifactVersion)).thenReturn(artifactVersionEntity);
        when(this.artifactVersionJpaRepository
                .findFirstByArtifactIdAndRepositoryIdOrderByMilestoneDesc(artifactId, REPOID))
                .thenReturn(artifactVersionEntity);

        this.bpmnArtifactVersionService.createInitialVersion(artifactVersionCreation);
        //verify(mapper, times(1)).toEntity(bpmnArtifactVersion);
        //verify(bpmnArtifactVersionJpa, times(1)).save(bpmnArtifactVersionEntity);

    }

}
