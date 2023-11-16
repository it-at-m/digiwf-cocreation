package de.muenchen.oss.digiwf.cocreation.core.repository;

import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.service.ArtifactMilestoneService;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.service.ArtifactService;
import de.muenchen.oss.digiwf.cocreation.core.assignment.AssignmentBuilder;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.facade.RepositoryFacade;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.NewRepository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.Repository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.RepositoryUpdate;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.service.AssignmentService;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.service.AuthService;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.service.RepositoryService;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.AssignmentEntity;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.AssignmentId;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.repository.AssignmentJpaRepository;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.repository.RepoJpaRepository;
import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest()
public class RepositoryFacadeTest {

    @InjectMocks
    private RepositoryFacade repositoryFacade;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private ArtifactMilestoneService artifactMilestoneService;

    @Mock
    private ArtifactService artifactService;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private NewRepository newRepository;

    @Mock
    private AssignmentJpaRepository assignmentJpa;

    @Mock
    private RepoJpaRepository repoJpaRepository;

    private static final String REPOID = "42";
    private static final String REPONAME = "repo name";
    private static final String REPODESC = "repository description";
    private static final String USERID = "12345";

    private static LocalDateTime DATE;

    @BeforeAll
    public static void init() {
        DATE = LocalDateTime.now();
    }

    @Test
    @DisplayName("Create new repo")
    public void createRepository() {
        this.newRepository = RepositoryBuilder.buildNewRepo(REPONAME, REPODESC);
        final Repository repository = RepositoryBuilder.buildRepo(REPOID, REPONAME, REPODESC, LocalDateTime.now(), LocalDateTime.now());
        final AssignmentId assignmentId = AssignmentBuilder.buildAssignmentId(USERID, REPOID);
        final AssignmentEntity assignment = AssignmentBuilder.buildAssignmentEntity(assignmentId, RoleEnum.MEMBER);
        final List<AssignmentEntity> assignmentList = new ArrayList<>();
        assignmentList.add(assignment);
        assignmentList.add(assignment);

        when(this.userService.getUserIdOfCurrentUser()).thenReturn(USERID);
        when(this.assignmentJpa.findAssignmentEntitiesByAssignmentId_UserIdEquals(USERID)).thenReturn(assignmentList);
        when(this.repositoryService.createRepository(this.newRepository)).thenReturn(repository);

        this.repositoryFacade.createRepository(this.newRepository, USERID);
        verify(this.repositoryService, times(1)).createRepository(any());
        verify(this.assignmentService, times(1)).createInitialAssignment(any());
    }

    @Test
    @DisplayName("Updating a repo")
    public void updateRepository() {
        final RepositoryUpdate repositoryUpdate = RepositoryBuilder.buildRepoUpdate(REPONAME, REPODESC);

        this.repositoryFacade.updateRepository(REPOID, repositoryUpdate);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(any(), any());
        verify(this.repositoryService, times(1)).updateRepository(any(), any());
    }

    @Test
    @DisplayName("Delete Repository")
    public void deleteRepo() {
        this.repositoryFacade.deleteRepository(REPOID);
        verify(this.artifactMilestoneService, times(1)).deleteAllByRepositoryId(REPOID);
        verify(this.artifactService, times(1)).deleteAllByRepositoryId(REPOID);
        verify(this.repositoryService, times(1)).deleteRepository(REPOID);
        verify(this.assignmentService, times(1)).deleteAllByRepositoryId(REPOID);
    }
}
