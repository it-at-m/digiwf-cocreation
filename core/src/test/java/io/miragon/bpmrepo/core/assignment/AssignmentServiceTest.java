package io.miragon.bpmrepo.core.assignment;

import io.miragon.bpmrepo.core.repository.domain.model.Assignment;
import io.miragon.bpmrepo.core.repository.domain.service.AssignmentService;
import io.miragon.bpmrepo.core.repository.domain.service.RepositoryService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.user.UserBuilder;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AssignmentServiceTest {


    @MockBean
    private UserService userService;

    @MockBean
    private RepositoryService repositoryService;

    @Autowired
    private AssignmentService assignmentService;


    private static final String REPOID = "42";
    private static final String USERID = "12345";
    private static final String OWNERID = "6789";
    private static final String OWNERNAME = "OWNER";
    private static final String USERNAME = "USER";
    private static final RoleEnum OWNERROLE = RoleEnum.OWNER;
    private static final RoleEnum ROLEAFTER = RoleEnum.MEMBER;
    private static final RoleEnum ROLEBEFORE = RoleEnum.VIEWER;

    final Assignment downgradeOwnerAssignment = AssignmentBuilder.buildAssignment(OWNERID, REPOID, ROLEBEFORE);
    final Assignment userAssignment = AssignmentBuilder.buildAssignment(USERID, REPOID, ROLEBEFORE);
    final Assignment upgradedUserAssignment = AssignmentBuilder.buildAssignment(USERID, REPOID, ROLEAFTER);

    public User owner = UserBuilder.buildUser(OWNERID, OWNERNAME);
    public User user = UserBuilder.buildUser(USERID, USERNAME);

    @Test
    public void tests() {
        this.create();
        this.checkCreatedObject();
        this.addNewUser();
        this.changeUserRole();
        this.testIfOwnerCanChangeOwnRole();
        this.testIfUserCanChangeOwnersRole();
    }

    @Transactional
    public void create() {
        //Create initial assignment (happens after a new repository is created)
        when(this.userService.getCurrentUser()).thenReturn(this.owner);
        this.assignmentService.createInitialAssignment(REPOID);
    }


    @Transactional
    public void checkCreatedObject() {
        //Check if the properties of the assignment are correct (Repository Id and role (Owner))
        final List<String> assignmentIds = this.assignmentService.getAllAssignedRepositoryIds(OWNERID);
        assertEquals(1, assignmentIds.size());
        assertEquals(REPOID, assignmentIds.get(0));
        final RoleEnum role = this.assignmentService.getUserRole(REPOID, OWNERID);
        assertEquals(OWNERROLE, role);
    }

    @Transactional
    public void addNewUser() {
        //Add a new user to the mocked repository that is owned by "OWNER"

        //"checkIfOperationIsAllowed" Method will use the Owner as acting user
        when(this.userService.getUserIdOfCurrentUser()).thenReturn(OWNERID);
        doNothing().when(this.repositoryService).updateAssignedUsers(any(), any());
        this.assignmentService.createAssignment(this.userAssignment);

        //Check if there are now two assignmentEntities for the specified repository
        final List<Assignment> assignments = this.assignmentService.getAllAssignedUsers(REPOID);
        assertEquals(2, assignments.size());

        //User should have the role "Viewer"
        final Assignment userAssignment = assignments.stream().filter(assignment -> assignment.getUserId() == USERID).collect(Collectors.toList()).get(0);
        assertEquals(RoleEnum.VIEWER, userAssignment.getRole());
    }

    @Transactional
    public void changeUserRole() {
        //Change the user role from Viewer to Member
        final Assignment newUserAssignment = this.assignmentService.updateAssignment(this.upgradedUserAssignment);
        assertEquals(ROLEAFTER, newUserAssignment.getRole());
    }

    @Transactional
    public void testIfOwnerCanChangeOwnRole() {
        //Users (Owners and Admins) are not allowed to change teir own role in an repository (neither to higher roles nor to lower roles),
        // they can just leave (remove themselves) the repository
        assertThrows(AccessRightException.class, () -> this.assignmentService.updateAssignment(this.downgradeOwnerAssignment));
    }

    @Transactional
    public void testIfUserCanChangeOwnersRole() {
        //"checkIfOperationIsAllowed" Method will use the user (with role member) as acting user now
        // => does not provide rights and should throw an accessRightException
        when(this.userService.getCurrentUser()).thenReturn(this.user);
        assertThrows(AccessRightException.class, () -> this.assignmentService.updateAssignment(this.downgradeOwnerAssignment));
    }


    /**
     * Delete Operations somehow required a separate function
     */
    @Test
    @Transactional
    public void removeUser() {
        //Create two Assignments (Owner and Viewer, as above
        when(this.userService.getCurrentUser()).thenReturn(this.owner);
        this.assignmentService.createInitialAssignment(REPOID);
        when(this.userService.getUserIdOfCurrentUser()).thenReturn(OWNERID);
        doNothing().when(this.repositoryService).updateAssignedUsers(any(), any());
        this.assignmentService.createAssignment(this.userAssignment);


        //User with the "Member" role is sending requests
        // => should throw an accessrightException
        when(this.userService.getCurrentUser()).thenReturn(this.user);
        assertThrows(AccessRightException.class, () -> this.assignmentService.deleteAssignment(REPOID, OWNERID));

        //Change acting user to Owner and remove User from repository
        when(this.userService.getCurrentUser()).thenReturn(this.owner);
        this.assignmentService.deleteAssignment(REPOID, USERID);
        assertTrue(this.assignmentService.getAssignment(REPOID, USERID).isEmpty());
        final List<Assignment> assignments = this.assignmentService.getAllAssignedUsers(REPOID);
        assertEquals(1, assignments.size());

    }
}
