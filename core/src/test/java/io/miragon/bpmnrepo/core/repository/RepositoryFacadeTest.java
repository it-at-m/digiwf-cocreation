package io.miragon.bpmnrepo.core.repository;

import io.miragon.bpmnrepo.core.assignment.AssignmentBuilder;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramService;
import io.miragon.bpmnrepo.core.diagram.domain.business.BpmnDiagramVersionService;
import io.miragon.bpmnrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmnrepo.core.repository.domain.business.AssignmentService;
import io.miragon.bpmnrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmnrepo.core.repository.domain.business.BpmnRepositoryService;
import io.miragon.bpmnrepo.core.repository.domain.facade.BpmnRepositoryFacade;
import io.miragon.bpmnrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmnrepo.core.repository.infrastructure.entity.AssignmentId;
import io.miragon.bpmnrepo.core.repository.infrastructure.repository.AssignmentJpa;
import io.miragon.bpmnrepo.core.repository.infrastructure.repository.BpmnRepoJpaRepository;
import io.miragon.bpmnrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmnrepo.core.user.domain.business.UserService;
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
    private BpmnRepositoryFacade bpmnRepositoryFacade;

    @Mock
    private BpmnRepositoryService bpmnRepositoryService;

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private BpmnDiagramVersionService bpmnDiagramVersionService;

    @Mock
    private BpmnDiagramService bpmnDiagramService;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private NewBpmnRepositoryTO newBpmnRepositoryTO;

    @Mock
    private AssignmentJpa assignmentJpa;

    @Mock
    private BpmnRepoJpaRepository bpmnRepoJpa;

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
        this.newBpmnRepositoryTO = RepositoryBuilder.buildNewRepoTO(REPONAME, REPODESC);
        final AssignmentId assignmentId = AssignmentBuilder.buildAssignmentId(USERID, REPOID);
        final AssignmentEntity assignment = AssignmentBuilder.buildAssignmentEntity(assignmentId, RoleEnum.MEMBER);
        final List<AssignmentEntity> assignmentList = new ArrayList<>();
        assignmentList.add(assignment);
        assignmentList.add(assignment);

        when(this.userService.getUserIdOfCurrentUser()).thenReturn(USERID);
        when(this.assignmentJpa.findAssignmentEntitiesByAssignmentId_UserIdEquals(USERID)).thenReturn(assignmentList);

        this.bpmnRepositoryFacade.createRepository(this.newBpmnRepositoryTO);
        verify(this.bpmnRepositoryService, times(1)).createRepository(any());
        verify(this.assignmentService, times(1)).createInitialAssignment(any());

    }

    @Test
    @DisplayName("Updating a repo")
    public void updateRepository() {
        final NewBpmnRepositoryTO newBpmnRepositoryTO = RepositoryBuilder.buildNewRepoTO(REPONAME, REPODESC);

        this.bpmnRepositoryFacade.updateRepository(REPOID, newBpmnRepositoryTO);
        verify(this.authService, times(1)).checkIfOperationIsAllowed(any(), any());
        verify(this.bpmnRepositoryService, times(1)).updateRepository(any(), any());
    }

    @Test
    @DisplayName("Delete Repository")
    public void deleteRepo() {
        this.bpmnRepositoryFacade.deleteRepository(REPOID);
        verify(this.bpmnDiagramVersionService, times(1)).deleteAllByRepositoryId(REPOID);
        verify(this.bpmnDiagramService, times(1)).deleteAllByRepositoryId(REPOID);
        verify(this.bpmnRepositoryService, times(1)).deleteRepository(REPOID);
        verify(this.assignmentService, times(1)).deleteAllByRepositoryId(REPOID);
    }
}
