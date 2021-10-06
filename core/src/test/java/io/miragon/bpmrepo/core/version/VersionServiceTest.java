package io.miragon.bpmrepo.core.version;

import io.miragon.bpmrepo.core.artifact.domain.enums.SaveTypeEnum;
import io.miragon.bpmrepo.core.artifact.domain.mapper.MilestoneMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestoneUpdate;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactMilestoneService;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactMilestoneEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.ArtifactMilestoneJpaRepository;
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
    private ArtifactMilestoneService artifactMilestoneService;

    @Mock
    private MilestoneMapper mapper;

    @Mock
    private ArtifactMilestoneJpaRepository artifactMilestoneJpaRepository;

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
        final ArtifactMilestoneEntity artifactMilestoneEntity = MilestoneBuilder
                .buildMilestoneEntity(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);
        final ArtifactMilestone artifactMilestone = MilestoneBuilder
                .buildMilestone(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);
        final ArtifactMilestoneUpdate artifactMilestoneUpdate = MilestoneBuilder.buildMilestoneUpdate(VERSIONID, COMMENT, FILESTRING);

        final ArgumentCaptor<ArtifactMilestoneEntity> captor = ArgumentCaptor.forClass(ArtifactMilestoneEntity.class);
        when(this.artifactMilestoneJpaRepository.findFirstByArtifactIdOrderByMilestoneDesc(artifactId))
                .thenReturn(Optional.of(artifactMilestoneEntity));
        when(this.mapper.mapToModel(artifactMilestoneEntity)).thenReturn(artifactMilestone);
        when(this.mapper.mapToEntity(artifactMilestone)).thenReturn(artifactMilestoneEntity);
        when(this.artifactMilestoneJpaRepository
                .findFirstByArtifactIdAndRepositoryIdOrderByMilestoneDesc(artifactId, REPOID))
                .thenReturn(artifactMilestoneEntity);
        when(this.artifactMilestoneJpaRepository.save(any())).thenReturn(artifactMilestoneEntity);
        when(this.artifactMilestoneJpaRepository.findById(any())).thenReturn(Optional.of(artifactMilestoneEntity));

        this.artifactMilestoneService.updateMilestone(artifactMilestoneUpdate);
        verify(this.mapper, times(2)).mapToModel(artifactMilestoneEntity);
        verify(this.artifactMilestoneJpaRepository, times(1)).save(captor.capture());
        verify(this.artifactMilestoneJpaRepository, times(1))
                .findById(anyString());

        final ArtifactMilestoneEntity savedVersionEntity = captor.getValue();
        assertNotNull(savedVersionEntity);
        assertEquals(savedVersionEntity.getFile(), FILESTRING);
    }

    @Test
    @Disabled
    public void createInitialVersion() {
        final ArtifactMilestoneEntity artifactMilestoneEntity = MilestoneBuilder
                .buildMilestoneEntity(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);
        final ArtifactMilestone artifactMilestone = MilestoneBuilder
                .buildMilestone(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);
        final ArtifactMilestone artifactMilestoneCreation = MilestoneBuilder
                .buildMilestone(VERSIONID, artifactId, REPOID, COMMENT, MILESTONE, FILESTRING, SAVETYPE);

        when(this.mapper.mapToEntity(artifactMilestone)).thenReturn(artifactMilestoneEntity);
        when(this.artifactMilestoneJpaRepository
                .findFirstByArtifactIdAndRepositoryIdOrderByMilestoneDesc(artifactId, REPOID))
                .thenReturn(artifactMilestoneEntity);

        this.artifactMilestoneService.createInitialMilestone(artifactMilestoneCreation);
        //verify(mapper, times(1)).toEntity(bpmnArtifactVersion);
        //verify(bpmnArtifactVersionJpa, times(1)).save(bpmnArtifactVersionEntity);

    }

}
