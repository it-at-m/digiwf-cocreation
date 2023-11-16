package de.muenchen.oss.digiwf.cocreation.core.repository.model;

import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.Repository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.RepositoryUpdate;
import de.muenchen.oss.digiwf.cocreation.core.repository.RepositoryBuilder;
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
        final RepositoryUpdate repositoryUpdate = RepositoryBuilder.buildRepoUpdate(NEWREPONAME, null);
        Repository repository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);

        //update name only
        repository.update(repositoryUpdate);
        assertEquals(NEWREPONAME, repository.getName());
        assertEquals(REPODESC, repository.getDescription());
        //update description only
        repository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        final RepositoryUpdate repositoryTODesc = RepositoryBuilder.buildRepoUpdate(null, NEWREPODESC);
        repository.update(repositoryTODesc);
        assertEquals(NEWREPODESC, repository.getDescription());
        assertEquals(REPONAME, repository.getName());
        //update both
        repository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, DATE, DATE);
        final RepositoryUpdate repositoryTOBoth = RepositoryBuilder.buildRepoUpdate(NEWREPONAME, NEWREPODESC);
        repository.update(repositoryTOBoth);
        assertEquals(NEWREPONAME, repository.getName());
        assertEquals(NEWREPODESC, repository.getDescription());
    }
}
