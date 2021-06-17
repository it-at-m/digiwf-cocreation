package io.miragon.bpmnrepo.core.repository;

import io.miragon.bpmnrepo.core.repository.api.transport.BpmnRepositoryRequestTO;
import io.miragon.bpmnrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmnrepo.core.repository.domain.business.BpmnRepositoryService;
import io.miragon.bpmnrepo.core.repository.domain.mapper.RepositoryMapper;
import io.miragon.bpmnrepo.core.repository.domain.model.BpmnRepository;
import io.miragon.bpmnrepo.core.repository.infrastructure.entity.BpmnRepositoryEntity;
import io.miragon.bpmnrepo.core.repository.infrastructure.repository.BpmnRepoJpa;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class RepositoryServiceTest {

    @InjectMocks
    private BpmnRepositoryService bpmnRepositoryService;

    @Mock
    private BpmnRepoJpa bpmnRepoJpa;

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
    public static void init(){
        DATE = LocalDateTime.now();
    }

    @Test
    public void createRepository(){
        NewBpmnRepositoryTO newBpmnRepositoryTO = RepositoryBuilder.buildNewRepoTO(REPONAME, REPODESC);
        BpmnRepository bpmnRepository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        BpmnRepositoryEntity bpmnRepositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);

        //als Parameter in Methode schreiben, um die Argumente mitzugeben und zusätzlich später auswerten zu können
        final ArgumentCaptor<BpmnRepository> captor = ArgumentCaptor.forClass(BpmnRepository.class);
        when(mapper.toModel(newBpmnRepositoryTO)).thenReturn(bpmnRepository);
        when(mapper.toEntity(bpmnRepository)).thenReturn(bpmnRepositoryEntity);
        when(bpmnRepositoryService.saveToDb(bpmnRepositoryEntity)).thenReturn(bpmnRepository);


        bpmnRepositoryService.createRepository(newBpmnRepositoryTO);
        verify(this.mapper, times(1)).toModel(newBpmnRepositoryTO);
        verify(this.mapper, times(1)).toEntity(captor.capture());

        final BpmnRepository savedRepo = captor.getValue();
        assertNotNull(savedRepo);
        assertNotNull(savedRepo.getBpmnRepositoryId());
        assertEquals(DATE, savedRepo.getCreatedDate());
        assertEquals(DATE, savedRepo.getUpdatedDate());

    }

    @Test
    public void updateRepository(){
        NewBpmnRepositoryTO newBpmnRepositoryTO = RepositoryBuilder.buildNewRepoTO(REPONAME, REPODESC);
        BpmnRepository bpmnRepository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        BpmnRepositoryEntity bpmnRepositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);

        when(bpmnRepoJpa.getOne(any())).thenReturn(bpmnRepositoryEntity);
        when(mapper.toModel(bpmnRepositoryEntity)).thenReturn(bpmnRepository);

        bpmnRepositoryService.updateRepository(REPOID, newBpmnRepositoryTO);
        verify(bpmnRepoJpa, times(1)).getOne(REPOID);
        verify(mapper, times(1)).toEntity(any());

        assertEquals(bpmnRepositoryEntity.getUpdatedDate(), DATE);
    }
    @Test
    public void getSingleRepository(){
        BpmnRepositoryEntity bpmnRepositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);
        BpmnRepositoryRequestTO bpmnRepositoryRequestTO = RepositoryBuilder.buildNewRepoRequestTO(REPOID, REPONAME, REPODESC, EXISTINGDIAGRAMS, ASSIGNEDUSERS);

        when(bpmnRepoJpa.findByBpmnRepositoryId(REPOID)).thenReturn(bpmnRepositoryEntity);
        when(mapper.toRequestTO(bpmnRepositoryEntity)).thenReturn(bpmnRepositoryRequestTO);

        bpmnRepositoryService.getSingleRepository(REPOID);
        verify(bpmnRepoJpa, times(1)).findByBpmnRepositoryId(REPOID);
        verify(mapper, times(1)).toRequestTO(bpmnRepositoryEntity);

    }

    @Test
    public void deleteRepository(){
        bpmnRepositoryService.deleteRepository(REPOID);
        verify(bpmnRepoJpa, times(1)).deleteBpmnRepositoryEntityByBpmnRepositoryId(REPOID);
        assertEquals(Optional.empty(), bpmnRepoJpa.findByBpmnRepositoryIdEquals(REPOID));
    }

    @Test
    public void saveToDb(){
        BpmnRepositoryEntity bpmnRepositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);

        when(bpmnRepoJpa.save(bpmnRepositoryEntity)).thenReturn(bpmnRepositoryEntity);

        bpmnRepositoryService.saveToDb(bpmnRepositoryEntity);
        verify(bpmnRepoJpa, times(1)).save(bpmnRepositoryEntity);
        verify(mapper, times(1)).toModel(bpmnRepositoryEntity);

    }


}
