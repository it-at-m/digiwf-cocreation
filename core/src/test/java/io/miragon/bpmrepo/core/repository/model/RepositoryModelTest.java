package io.miragon.bpmrepo.core.repository.model;

import io.miragon.bpmrepo.core.repository.RepositoryBuilder;
import io.miragon.bpmrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmrepo.core.repository.domain.model.BpmnRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RepositoryModelTest {

    private static final String REPOID = "42";
    private static final String REPONAME = "repo name";
    private static final String NEWREPONAME = "new name";
    private static final String REPODESC = "repository description";
    private static final String NEWREPODESC = "new description";
    private static LocalDateTime DATE;

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    public void updateRepository() {
        final NewBpmnRepositoryTO newBpmnRepositoryTOName = RepositoryBuilder.buildNewRepoTO(NEWREPONAME, null);
        BpmnRepository bpmnRepository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);

        //update name only
        bpmnRepository.update(newBpmnRepositoryTOName);
        assertEquals(NEWREPONAME, bpmnRepository.getBpmnRepositoryName());
        assertEquals(REPODESC, bpmnRepository.getBpmnRepositoryDescription());
        //update description only
        bpmnRepository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        final NewBpmnRepositoryTO newBpmnRepositoryTODesc = RepositoryBuilder.buildNewRepoTO(null, NEWREPODESC);
        bpmnRepository.update(newBpmnRepositoryTODesc);
        assertEquals(NEWREPODESC, bpmnRepository.getBpmnRepositoryDescription());
        assertEquals(REPONAME, bpmnRepository.getBpmnRepositoryName());
        //update both
        bpmnRepository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        final NewBpmnRepositoryTO newBpmnRepositoryTOBoth = RepositoryBuilder.buildNewRepoTO(NEWREPONAME, NEWREPODESC);
        bpmnRepository.update(newBpmnRepositoryTOBoth);
        assertEquals(NEWREPONAME, bpmnRepository.getBpmnRepositoryName());
        assertEquals(NEWREPODESC, bpmnRepository.getBpmnRepositoryDescription());
    }
}
