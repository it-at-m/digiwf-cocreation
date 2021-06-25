package io.miragon.bpmrepo.core.repository;

import io.miragon.bpmrepo.core.assignment.AssignmentBuilder;
import io.miragon.bpmrepo.core.diagram.domain.business.DiagramService;
import io.miragon.bpmrepo.core.diagram.domain.business.DiagramVersionService;
import io.miragon.bpmrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmrepo.core.repository.domain.business.AssignmentService;
import io.miragon.bpmrepo.core.repository.domain.business.AuthService;
import io.miragon.bpmrepo.core.repository.domain.business.RepositoryService;
import io.miragon.bpmrepo.core.repository.domain.facade.BpmnRepositoryFacade;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentId;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.AssignmentJpa;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.BpmnRepoJpaRepository;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
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
    private RepositoryService bpmnRepositoryService;

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private DiagramVersionService bpmnDiagramVersionService;

    @Mock
    private DiagramService bpmnDiagramService;

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
