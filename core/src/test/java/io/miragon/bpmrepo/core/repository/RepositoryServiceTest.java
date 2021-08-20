package io.miragon.bpmrepo.core.repository;

import io.miragon.bpmrepo.core.repository.domain.mapper.RepositoryMapper;
import io.miragon.bpmrepo.core.repository.domain.model.NewRepository;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.model.RepositoryUpdate;
import io.miragon.bpmrepo.core.repository.domain.service.RepositoryService;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.RepositoryEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.RepoJpaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RepositoryServiceTest {

    @InjectMocks
    private RepositoryService repositoryService;

    @Mock
    private RepoJpaRepository repoJpaRepository;

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
        final NewRepository newRepository = RepositoryBuilder.buildNewRepo(REPONAME, REPODESC);
        final Repository repository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        final RepositoryEntity repositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);

        //als Parameter in Methode schreiben, um die Argumente mitzugeben und zusätzlich später auswerten zu können
        final ArgumentCaptor<Repository> captor = ArgumentCaptor.forClass(Repository.class);
        when(this.mapper.mapToEntity(repository)).thenReturn(repositoryEntity);
        when(this.repositoryService.saveToDb(repository)).thenReturn(repository);

        this.repositoryService.createRepository(newRepository);
    }

    @Test
    public void updateRepository() {
        final RepositoryUpdate repositoryUpdate = RepositoryBuilder.buildRepoUpdate(REPONAME, REPODESC);
        final Repository repository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        final RepositoryEntity repositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);

        when(this.repoJpaRepository.findById(any())).thenReturn(Optional.of(repositoryEntity));
        when(this.mapper.mapToModel(repositoryEntity)).thenReturn(repository);

        this.repositoryService.updateRepository(REPOID, repositoryUpdate);
        verify(this.repoJpaRepository, times(1)).findById(REPOID);
        verify(this.mapper, times(1)).mapToEntity(any());

        assertEquals(repositoryEntity.getUpdatedDate(), DATE);
    }

    @Test
    public void getSingleRepository() {
        final RepositoryEntity repositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);
        final Repository repository = RepositoryBuilder
                .buildRepo(REPOID, REPONAME, REPODESC, LocalDateTime.now(), LocalDateTime.now());

        when(this.repoJpaRepository.findById(REPOID)).thenReturn(Optional.of(repositoryEntity));
        when(this.mapper.mapToModel(repositoryEntity)).thenReturn(repository);

        this.repositoryService.getRepository(REPOID);
        verify(this.repoJpaRepository, times(1)).findById(REPOID);
        verify(this.mapper, times(1)).mapToModel(repositoryEntity);

    }

    @Test
    public void deleteRepository() {
        this.repositoryService.deleteRepository(REPOID);
        verify(this.repoJpaRepository, times(1)).deleteById(REPOID);
        assertEquals(Optional.empty(), this.repoJpaRepository.findById(REPOID));
    }

    @Test
    public void saveToDb() {
        final RepositoryEntity repositoryEntity = RepositoryBuilder.buildRepoEntity(REPOID, REPONAME, REPODESC, DATE, DATE);
        final Repository repository = RepositoryBuilder
                .buildRepo(REPOID, REPONAME, REPODESC, LocalDateTime.now(), LocalDateTime.now());
        when(this.mapper.mapToEntity(repository)).thenReturn(repositoryEntity);

        when(this.repoJpaRepository.save(repositoryEntity)).thenReturn(repositoryEntity);

        this.repositoryService.saveToDb(repository);
        verify(this.repoJpaRepository, times(1)).save(repositoryEntity);
        verify(this.mapper, times(1)).mapToModel(repositoryEntity);

    }

}
