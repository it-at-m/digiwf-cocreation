package io.miragon.bpmrepo.core.EndToEnd;


import io.miragon.bpmrepo.core.artifact.ArtifactBuilder;
import io.miragon.bpmrepo.core.artifact.api.resource.ArtifactController;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTO;
import io.miragon.bpmrepo.core.artifact.api.transport.NewArtifactTO;
import io.miragon.bpmrepo.core.assignment.AssignmentBuilder;
import io.miragon.bpmrepo.core.repository.RepositoryBuilder;
import io.miragon.bpmrepo.core.repository.api.resource.AssignmentController;
import io.miragon.bpmrepo.core.repository.api.resource.RepositoryController;
import io.miragon.bpmrepo.core.repository.api.transport.AssignmentTO;
import io.miragon.bpmrepo.core.repository.api.transport.NewRepositoryTO;
import io.miragon.bpmrepo.core.repository.api.transport.RepositoryTO;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.AssignmentJpaRepository;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import io.miragon.bpmrepo.core.user.UserBuilder;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
public class RepositoryIntegrationTest {

    @MockBean
    private UserService userService;

    @Autowired
    private AssignmentJpaRepository assignmentJpaRepository;

    @Autowired
    private RepositoryController repositoryController;

    @Autowired
    private ArtifactController artifactController;

    @Autowired
    private AssignmentController assignmentController;


    private static final String REPONAME = "NAME";
    private static final String REPODESC = "DESC";

    private static final String OWNERID = "123";
    private static final String OWNERNAME = "USER";

    private static final String MEMBERID = "456";
    private static final String MEMBERNAME = "MEMBER";
    private static final RoleEnum MEMBERROLE = RoleEnum.MEMBER;

    private static final String ARTIFACTNAME = "ARTIFACT";
    private static final String ARTIFACTDESC = "ARTIFACTDESC";
    private static final String FILETYPE = "BPMN";


    private static final User owner = UserBuilder.buildUser(OWNERID, OWNERNAME);
    private static final User member = UserBuilder.buildUser(MEMBERID, MEMBERNAME);
    private static final NewRepositoryTO newRepositoryTO = RepositoryBuilder.buildNewRepoTO(REPONAME, REPODESC);
    private static final NewArtifactTO newArtifactTO = ArtifactBuilder.buildNewArtifactTO(ARTIFACTNAME, ARTIFACTDESC, FILETYPE);

    @Test
    public void tests() {
        this.createRepository();
        final RepositoryTO repositoryTO = this.getAllRepositories();
        final ArtifactTO artifactTO = this.createArtifact(repositoryTO.getId());
        this.getAssignments(repositoryTO.getId());
        this.addUser(repositoryTO.getId());
        this.forbidMemberToDeleteAnything(repositoryTO.getId(), artifactTO.getId());
        this.deleteArtifact(artifactTO.getId());
        this.removeUser(repositoryTO.getId());
        this.deleteRepository(repositoryTO.getId());
    }


    @Transactional
    public void createRepository() {
        when(this.userService.getUserIdOfCurrentUser()).thenReturn(OWNERID);
        when(this.userService.getCurrentUser()).thenReturn(owner);

        final ResponseEntity<RepositoryTO> response = this.repositoryController.createRepository(newRepositoryTO);
        final RepositoryTO repositoryTO = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(repositoryTO);
        assertNotNull(repositoryTO.getId());
        assertEquals(REPONAME, repositoryTO.getName());
        assertEquals(REPODESC, repositoryTO.getDescription());
    }

    @Transactional
    public RepositoryTO getAllRepositories() {
        final ResponseEntity<List<RepositoryTO>> response = this.repositoryController.getAllRepositories();
        final List<RepositoryTO> repositoryTOList = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, repositoryTOList.size());
        assertNotNull(repositoryTOList.get(0));
        assertNotNull(repositoryTOList.get(0).getId());
        return repositoryTOList.get(0);
    }

    @Transactional
    public ArtifactTO createArtifact(final String repositoryId) {
        final ResponseEntity<ArtifactTO> response = this.artifactController.createArtifact(repositoryId, newArtifactTO);
        final ArtifactTO artifactTO = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(artifactTO);
        assertNotNull(artifactTO.getId());
        assertEquals(repositoryId, artifactTO.getRepositoryId());
        assertEquals(ARTIFACTNAME, artifactTO.getName());
        return artifactTO;
    }

    @Transactional
    public void getAssignments(final String repositoryId) {
        System.out.println(this.assignmentJpaRepository.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(repositoryId, OWNERID).get().getRole());
        System.out.println(this.assignmentJpaRepository.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(repositoryId, OWNERID).get().getAssignmentId().getRepositoryId());
        System.out.println(this.assignmentJpaRepository.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(repositoryId, OWNERID).get().getAssignmentId().getUserId());
    }

    @Transactional
    public void addUser(final String repositoryId) {
        final AssignmentTO assignmentTO = AssignmentBuilder.buildAssignmentTO(repositoryId, MEMBERID, MEMBERROLE);

        final ResponseEntity<AssignmentTO> response = this.assignmentController.createUserAssignment(assignmentTO);
        final AssignmentTO assignment = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(assignment);
        assertEquals(assignment.getUserId(), MEMBERID);
        assertEquals(assignment.getRepositoryId(), repositoryId);
        assertEquals(assignment.getRole(), assignmentTO.getRole());
    }

    @Transactional
    public void forbidMemberToDeleteAnything(final String repositoryId, final String artifactId) {
        //UserService now returns the Member as currently acting user
        when(this.userService.getUserIdOfCurrentUser()).thenReturn(MEMBERID);
        when(this.userService.getCurrentUser()).thenReturn(member);

        //Trying to delete an artifact or the whole repository throws an AccessRightExceptions
        assertThrows(AccessRightException.class, () -> this.artifactController.deleteArtifact(artifactId));
        assertThrows(AccessRightException.class, () -> this.repositoryController.deleteRepository(repositoryId));
        assertThrows(AccessRightException.class, () -> this.assignmentController.deleteUserAssignment(repositoryId, OWNERID));

        //Return the Owner as acting user again
        when(this.userService.getUserIdOfCurrentUser()).thenReturn(OWNERID);
        when(this.userService.getCurrentUser()).thenReturn(owner);
    }

    @Transactional
    public void deleteArtifact(final String artifactId) {
        final ResponseEntity<Void> response = this.artifactController.deleteArtifact(artifactId);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //Querying the deleted artifact throws an exception
        assertThrows(ObjectNotFoundException.class, () -> this.artifactController.getArtifact(artifactId));
    }

    @Transactional
    public void removeUser(final String repositoryId) {
        final ResponseEntity<Void> response = this.assignmentController.deleteUserAssignment(repositoryId, MEMBERID);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final ResponseEntity<List<AssignmentTO>> response2 = this.assignmentController.getAllAssignedUsers(repositoryId);
        final List<AssignmentTO> assignmentTOS = response2.getBody();
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(1, assignmentTOS.size());
    }

    @Transactional
    public void deleteRepository(final String repositoryId) {
        final ResponseEntity<Void> response = this.repositoryController.deleteRepository(repositoryId);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertThrows(ObjectNotFoundException.class, () -> this.repositoryController.getSingleRepository(repositoryId));

        /*
        During the getAllAssignedUsers method it will be checked if the requesting user is assigned to the specified repository 
        As the repository has been deleted before - and therefore also all existing assignments to this repository -
        the authService will throw an accessRightException, because it can't find an assignment between requesting user and the provided repository
        it is not being considered that the requested repository does not even exist.
        => This assertion implies that all assignments are automatically deleted after a repository has been deleted
         */
        assertThrows(AccessRightException.class, () -> this.assignmentController.getAllAssignedUsers(repositoryId));

    }


}
