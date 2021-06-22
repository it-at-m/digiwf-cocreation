package io.miragon.bpmnrepo.core.repository;

import io.miragon.bpmnrepo.core.repository.api.transport.BpmnRepositoryRequestTO;
import io.miragon.bpmnrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmnrepo.core.repository.domain.business.BpmnRepositoryService;
import io.miragon.bpmnrepo.core.repository.domain.mapper.RepositoryMapper;
import io.miragon.bpmnrepo.core.repository.domain.model.BpmnRepository;
import io.miragon.bpmnrepo.core.repository.infrastructure.entity.BpmnRepositoryEntity;
import io.miragon.bpmnrepo.core.repository.infrastructure.repository.BpmnRepoJpaRepository;
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
public class RepositoryServiceTest {

    @InjectMocks
    private BpmnRepositoryService bpmnRepositoryService;

    @Mock
    private BpmnRepoJpaRepository bpmnRepoJpa;

    @Mock
    private RepositoryMapper mapper;

    private static final String REPOID = "42";
    private static final String REPONAME = "repo name";
    private static final String REPODESC = "repository description";
    private static final Integer EXISTINGDIAGRAMS = 5;
    private static final Integer ASSIGNEDUSERS = 3;
    private static final String USERID = "12345";
    private static LocalDateTime DATE;

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void createRepository() {
        final NewBpmnRepositoryTO newBpmnRepositoryTO = RepositoryBuilder.buildNewRepoTO(REPONAME, REPODESC);
        final BpmnRepository bpmnRepository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        final BpmnRepositoryEntity bpmnRepositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);

        //als Parameter in Methode schreiben, um die Argumente mitzugeben und zusätzlich später auswerten zu können
        final ArgumentCaptor<BpmnRepository> captor = ArgumentCaptor.forClass(BpmnRepository.class);
        when(this.mapper.toModel(newBpmnRepositoryTO)).thenReturn(bpmnRepository);
        when(this.mapper.toEntity(bpmnRepository)).thenReturn(bpmnRepositoryEntity);
        when(this.bpmnRepositoryService.saveToDb(bpmnRepositoryEntity)).thenReturn(bpmnRepository);

        this.bpmnRepositoryService.createRepository(newBpmnRepositoryTO);
        verify(this.mapper, times(1)).toModel(newBpmnRepositoryTO);
        verify(this.mapper, times(1)).toEntity(captor.capture());

        final BpmnRepository savedRepo = captor.getValue();
        assertNotNull(savedRepo);
        assertNotNull(savedRepo.getBpmnRepositoryId());
        assertEquals(DATE, savedRepo.getCreatedDate());
        assertEquals(DATE, savedRepo.getUpdatedDate());

    }

    @Test
    public void updateRepository() {
        final NewBpmnRepositoryTO newBpmnRepositoryTO = RepositoryBuilder.buildNewRepoTO(REPONAME, REPODESC);
        final BpmnRepository bpmnRepository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        final BpmnRepositoryEntity bpmnRepositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);

        when(this.bpmnRepoJpa.getOne(any())).thenReturn(bpmnRepositoryEntity);
        when(this.mapper.toModel(bpmnRepositoryEntity)).thenReturn(bpmnRepository);

        this.bpmnRepositoryService.updateRepository(REPOID, newBpmnRepositoryTO);
        verify(this.bpmnRepoJpa, times(1)).getOne(REPOID);
        verify(this.mapper, times(1)).toEntity(any());

        assertEquals(bpmnRepositoryEntity.getUpdatedDate(), DATE);
    }

    @Test
    public void getSingleRepository() {
        final BpmnRepositoryEntity bpmnRepositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);
        final BpmnRepositoryRequestTO bpmnRepositoryRequestTO = RepositoryBuilder
                .buildNewRepoRequestTO(REPOID, REPONAME, REPODESC, EXISTINGDIAGRAMS, ASSIGNEDUSERS);

        when(this.bpmnRepoJpa.findByBpmnRepositoryId(REPOID)).thenReturn(bpmnRepositoryEntity);
        when(this.mapper.toRequestTO(bpmnRepositoryEntity)).thenReturn(bpmnRepositoryRequestTO);

        this.bpmnRepositoryService.getSingleRepository(REPOID);
        verify(this.bpmnRepoJpa, times(1)).findByBpmnRepositoryId(REPOID);
        verify(this.mapper, times(1)).toRequestTO(bpmnRepositoryEntity);

    }

    @Test
    public void deleteRepository() {
        this.bpmnRepositoryService.deleteRepository(REPOID);
        verify(this.bpmnRepoJpa, times(1)).deleteBpmnRepositoryEntityByBpmnRepositoryId(REPOID);
        assertEquals(Optional.empty(), this.bpmnRepoJpa.findByBpmnRepositoryIdEquals(REPOID));
    }

    @Test
    public void saveToDb() {
        final BpmnRepositoryEntity bpmnRepositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);

        when(this.bpmnRepoJpa.save(bpmnRepositoryEntity)).thenReturn(bpmnRepositoryEntity);

        this.bpmnRepositoryService.saveToDb(bpmnRepositoryEntity);
        verify(this.bpmnRepoJpa, times(1)).save(bpmnRepositoryEntity);
        verify(this.mapper, times(1)).toModel(bpmnRepositoryEntity);

    }

}
