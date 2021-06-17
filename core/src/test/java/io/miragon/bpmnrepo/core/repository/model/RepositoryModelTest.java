package io.miragon.bpmnrepo.core.repository.model;

import io.miragon.bpmnrepo.core.repository.RepositoryBuilder;
import io.miragon.bpmnrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmnrepo.core.repository.domain.model.BpmnRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
public class RepositoryModelTest {

    private static final String REPOID = "42";
    private static final String REPONAME = "repo name";
    private static final String NEWREPONAME = "new name";
    private static final String REPODESC = "repository description";
    private static final String NEWREPODESC = "new description";
    private static LocalDateTime DATE;


    @BeforeAll
    public static void init(){
        DATE = LocalDateTime.now();
    }


    @Test
    public void updateRepository(){
        NewBpmnRepositoryTO newBpmnRepositoryTOName = RepositoryBuilder.buildNewRepoTO(NEWREPONAME, null);
        BpmnRepository bpmnRepository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);

        //update name only
        bpmnRepository.updateRepo(newBpmnRepositoryTOName);
        assertEquals(NEWREPONAME, bpmnRepository.getBpmnRepositoryName());
        assertEquals(REPODESC, bpmnRepository.getBpmnRepositoryDescription());
        //update description only
        bpmnRepository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        NewBpmnRepositoryTO newBpmnRepositoryTODesc = RepositoryBuilder.buildNewRepoTO(null, NEWREPODESC);
        bpmnRepository.updateRepo(newBpmnRepositoryTODesc);
        assertEquals(NEWREPODESC, bpmnRepository.getBpmnRepositoryDescription());
        assertEquals(REPONAME, bpmnRepository.getBpmnRepositoryName());
        //update both
        bpmnRepository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        NewBpmnRepositoryTO newBpmnRepositoryTOBoth = RepositoryBuilder.buildNewRepoTO(NEWREPONAME, NEWREPODESC);
        bpmnRepository.updateRepo(newBpmnRepositoryTOBoth);
        assertEquals(NEWREPONAME, bpmnRepository.getBpmnRepositoryName());
        assertEquals(NEWREPODESC, bpmnRepository.getBpmnRepositoryDescription());
    }
}
