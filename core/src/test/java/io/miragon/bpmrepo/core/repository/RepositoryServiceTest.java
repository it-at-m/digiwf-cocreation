package io.miragon.bpmrepo.core.repository;

import io.miragon.bpmrepo.core.repository.domain.mapper.RepositoryMapper;
import io.miragon.bpmrepo.core.repository.domain.model.NewRepository;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.model.RepositoryUpdate;
import io.miragon.bpmrepo.core.repository.domain.service.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RepositoryServiceTest {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RepositoryMapper mapper;

    private static final String UNKNOWNREPOID = "42";
    private static final String REPONAME = "repo name";
    private static final String REPONAMEUPDATE = "updated name";
    private static final String REPODESC = "repository description";
    private static final String REPODESCUPDATE = "updated description";
    private static LocalDateTime DATE;


    public NewRepository newRepository = RepositoryBuilder.buildNewRepo(REPONAME, REPODESC);
    public RepositoryUpdate repositoryUpdate = RepositoryBuilder.buildRepoUpdate(REPONAMEUPDATE, REPODESCUPDATE);

    @Test
    public void tests() {
        final String id = this.create();
        this.getRepository(id);
        this.update(id);
        this.delete(id);
    }


    @Transactional
    public String create() {
        //Create a new repository
        final Repository createdRepository = this.repositoryService.createRepository(this.newRepository);
        assertNotNull(createdRepository.getId());
        return createdRepository.getId();
    }

    @Test
    public void getRepository(final String id) {
        //Check if repository can be found
        final Repository repository = this.repositoryService.getRepository(id).get();
        assertNotNull(repository);
        assertEquals(REPONAME, repository.getName());
        assertEquals(REPODESC, repository.getDescription());
        //Check if an the object is empty if a user passes an unknown id
        assertTrue(this.repositoryService.getRepository(UNKNOWNREPOID).isEmpty());

    }

    @Transactional
    public void update(final String id) {
        final Repository updatedRepository = this.repositoryService.updateRepository(id, this.repositoryUpdate);
        assertEquals(REPONAMEUPDATE, updatedRepository.getName());
        assertEquals(REPODESCUPDATE, updatedRepository.getDescription());
    }

    @Transactional
    public void delete(final String id) {
        this.repositoryService.deleteRepository(id);
        //assert the repository does not exist anymore
        assertTrue(this.repositoryService.getRepository(id).isEmpty());

    }
}
