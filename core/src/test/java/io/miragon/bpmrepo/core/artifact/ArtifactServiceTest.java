package io.miragon.bpmrepo.core.artifact;

import io.miragon.bpmrepo.core.artifact.domain.mapper.ArtifactMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactUpdate;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.ArtifactJpaRepository;
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
public class ArtifactServiceTest {

    @InjectMocks
    private ArtifactService artifactService;

    @Mock
    private ArtifactJpaRepository artifactJpaRepository;

    @Mock
    private ArtifactMapper mapper;

    private static final String artifactId = "123456";
    private static final String REPOID = "01";
    private static final String artifactName = "TestArtifact";
    private static final String DIAGRAMDESC = "SomeDescription";
    private static LocalDateTime DATE;

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void createArtifact() {
        final Artifact artifact = ArtifactBuilder
                .buildArtifact(artifactId, REPOID, artifactName, DIAGRAMDESC, DATE, DATE);
        final ArtifactEntity artifactEntity = ArtifactBuilder.buildArtifactEntity(artifactId, REPOID, artifactName, DIAGRAMDESC, DATE, DATE);

        final ArgumentCaptor<Artifact> captor = ArgumentCaptor.forClass(Artifact.class);
        when(this.mapper.mapToModel(any(ArtifactEntity.class))).thenReturn(artifact);
        when(this.mapper.mapToEntity(artifact)).thenReturn(artifactEntity);
        when(this.artifactJpaRepository.save(any())).thenReturn(artifactEntity);

        this.artifactService.createArtifact(artifact);
        verify(this.mapper, times(1)).mapToModel(artifactEntity);
        verify(this.mapper, times(1)).mapToEntity(captor.capture());
        verify(this.artifactJpaRepository, times(1)).save(artifactEntity);

        final Artifact savedArtifact = captor.getValue();
        assertNotNull(savedArtifact);
        assertEquals(savedArtifact.getUpdatedDate(), DATE);
        assertEquals(savedArtifact.getCreatedDate(), DATE);
    }

    @Test
    public void updateArtifact() {
        final ArtifactUpdate artifactUpdate = ArtifactBuilder.buildArtifactUpdate(artifactName, DIAGRAMDESC);
        final ArtifactEntity artifactEntity = ArtifactBuilder.buildArtifactEntity(artifactId, REPOID, artifactName, DIAGRAMDESC, DATE, DATE);
        final Artifact artifact = ArtifactBuilder
                .buildArtifact(artifactId, REPOID, artifactName, DIAGRAMDESC, DATE, DATE);

        final ArgumentCaptor<ArtifactEntity> captor = ArgumentCaptor.forClass(ArtifactEntity.class);
        when(this.artifactJpaRepository.findById(any())).thenReturn(Optional.of(artifactEntity));
        when(this.mapper.mapToModel(artifactEntity)).thenReturn(artifact);
        when(this.mapper.mapToEntity(artifact)).thenReturn(artifactEntity);

        this.artifactService.updateArtifact(artifact, artifactUpdate);
        verify(this.artifactJpaRepository, times(1)).findById(artifactId);
        verify(this.artifactJpaRepository, times(1)).save(captor.capture());

        final ArtifactEntity savedArtifact = captor.getValue();
        assertEquals(REPOID, savedArtifact.getRepositoryId());
        assertEquals(DATE, savedArtifact.getUpdatedDate());

    }

    @Test
    public void getSingleArtifact() {
        final ArtifactEntity artifactEntity = ArtifactBuilder.buildArtifactEntity(artifactId, REPOID, artifactName, DIAGRAMDESC, DATE, DATE);
        final Artifact artifact = ArtifactBuilder.buildArtifact(artifactId, REPOID, artifactName, DIAGRAMDESC, LocalDateTime.now(), LocalDateTime.now());

        when(this.artifactJpaRepository.findById(artifactId)).thenReturn(Optional.of(artifactEntity));
        when(this.mapper.mapToModel(artifactEntity)).thenReturn(artifact);

        this.artifactService.getArtifactById(artifactId);
        verify(this.artifactJpaRepository, times(1)).findById(artifactId);
        verify(this.mapper, times(1)).mapToModel(artifactEntity);
    }

    @Test
    public void deleteArtifact() {
        doNothing().when(this.artifactJpaRepository).deleteById(artifactId);

        this.artifactService.deleteArtifact(artifactId);
        verify(this.artifactJpaRepository, times(1)).deleteById(artifactId);
    }

    @Test
    public void deleteAllByArtifactId() {
        when(this.artifactJpaRepository.deleteAllByRepositoryId(REPOID)).thenReturn(any(Integer.class));
        this.artifactService.deleteAllByRepositoryId(REPOID);
        verify(this.artifactJpaRepository, times(1)).deleteAllByRepositoryId(REPOID);

    }
}
