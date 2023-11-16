package de.muenchen.oss.digiwf.cocreation.core.controller;

import de.muenchen.oss.digiwf.cocreation.core.artifact.ArtifactBuilder;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.resource.ArtifactController;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.resource.ArtifactMilestoneController;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.*;
import de.muenchen.oss.digiwf.cocreation.core.assignment.AssignmentBuilder;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.service.RepositoryService;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.AssignmentEntity;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.AssignmentId;
import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.repository.AssignmentJpaRepository;
import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.ObjectNotFoundException;
import de.muenchen.oss.digiwf.cocreation.core.user.UserBuilder;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.model.User;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.service.UserService;
import de.muenchen.oss.digiwf.cocreation.core.version.MilestoneBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ArtifactIntegrationTest {

    @MockBean
    private UserService userService;

    @MockBean
    private AssignmentJpaRepository assignmentJpaRepository;

    @MockBean
    private RepositoryService repositoryService;

    @Autowired
    private ArtifactController artifactController;

    @Autowired
    private ArtifactMilestoneController milestoneController;


    private static final String OWNERID = "123";
    private static final String OWNERNAME = "USER";
    private static final RoleEnum OWNERROLE = RoleEnum.OWNER;


    private static final String MEMBERID = "456";
    private static final String MEMBERNAME = "MEMBER";
    private static final RoleEnum MEMBERROLE = RoleEnum.MEMBER;

    private static final String REPOSITORYID = "111";

    private static final String ARTIFACTNAME = "ARTIFACT";
    private static final String ARTIFACTDESC = "ARTIFACTDESC";
    private static final String FILETYPE = "BPMN";

    private static final String COMMENT = "COMMENT";
    private static final String FILE = "<xml>abc</xml>";

    private static final String FILE2 = "<xml>abcdef</xml>";

    private static final String FILEUPDATE = "filestring";

    private static final User owner = UserBuilder.buildUser(OWNERID, OWNERNAME);
    private static final User member = UserBuilder.buildUser(MEMBERID, MEMBERNAME);

    private static final AssignmentId ownerAssignmentId = AssignmentBuilder.buildAssignmentId(OWNERID, REPOSITORYID);
    private static final AssignmentEntity ownerAssignment = AssignmentBuilder.buildAssignmentEntity(ownerAssignmentId, OWNERROLE);

    private static final AssignmentId memberAssignmentId = AssignmentBuilder.buildAssignmentId(MEMBERID, REPOSITORYID);
    private static final AssignmentEntity memberAssignment = AssignmentBuilder.buildAssignmentEntity(memberAssignmentId, MEMBERROLE);

    private static final NewArtifactTO newArtifactTO = ArtifactBuilder.buildNewArtifactTO(ARTIFACTNAME, ARTIFACTDESC, FILETYPE);

    private static final ArtifactMilestoneUploadTO milestoneTO = MilestoneBuilder.buildMilestoneUploadTO(COMMENT, FILE);
    private static final ArtifactMilestoneUploadTO milestoneTO2 = MilestoneBuilder.buildMilestoneUploadTO("", FILE2);



    /*
    Create Artifact
    Create Milestone
    Create second Milestone
    get milestone
    Update milestone
    Share artifact
    Delete artifact
     */

    @Test
    public void tests() {
        final ArtifactTO artifactTO = this.createArtifact();
        final ArtifactMilestoneTO milestoneTO = this.createSecondMilestone(artifactTO.getId());
        final ArtifactMilestoneTO milestoneTO2 = this.createThirdMilestone(artifactTO.getId(), milestoneTO.getId());
        final ArtifactMilestoneTO returnedMilestone = this.getMilestone(milestoneTO2);
        final ArtifactMilestoneTO updatedMilestone = this.updateMilestone(returnedMilestone.getId(), returnedMilestone.getArtifactId());
        this.deleteArtifact(artifactTO.getId(), milestoneTO.getId(), updatedMilestone.getId());

    }

    @Transactional
    public ArtifactTO createArtifact() {
        when(this.userService.getUserIdOfCurrentUser()).thenReturn(OWNERID);
        when(this.userService.getCurrentUser()).thenReturn(owner);

        when(this.assignmentJpaRepository.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(REPOSITORYID, MEMBERID)).thenReturn(Optional.of(memberAssignment));
        when(this.assignmentJpaRepository.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(REPOSITORYID, OWNERID)).thenReturn(Optional.of(ownerAssignment));

        final ResponseEntity<ArtifactTO> response = this.artifactController.createArtifact(REPOSITORYID, newArtifactTO);
        final ArtifactTO artifactTO = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(artifactTO);
        assertNotNull(artifactTO.getId());
        assertEquals(ARTIFACTNAME, artifactTO.getName());
        assertEquals(FILETYPE, artifactTO.getFileType());
        return artifactTO;
    }

    @Transactional
    public ArtifactMilestoneTO createSecondMilestone(final String artifactId) {
        final ResponseEntity<ArtifactMilestoneTO> response = this.milestoneController.createMilestone(artifactId, milestoneTO);
        final ArtifactMilestoneTO milestone = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(artifactId, milestone.getArtifactId());
        assertEquals(REPOSITORYID, milestone.getRepositoryId());
        assertEquals(FILE, milestone.getFile());
        assertEquals(2, milestone.getMilestone());
        assertNotNull(milestone.getUpdatedDate());
        assertTrue(milestone.isLatestMilestone());

        return milestone;
    }

    @Transactional
    public ArtifactMilestoneTO createThirdMilestone(final String artifactId, final String oldMilestoneId) {
        final ResponseEntity<ArtifactMilestoneTO> response = this.milestoneController.createMilestone(artifactId, milestoneTO2);
        final ArtifactMilestoneTO milestone = response.getBody();

        final ResponseEntity<ArtifactMilestoneTO> response2 = this.milestoneController.getMilestone(artifactId, oldMilestoneId);
        final ArtifactMilestoneTO oldMilestone = response2.getBody();
        assertEquals(HttpStatus.OK, response2.getStatusCode());


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(artifactId, milestone.getArtifactId());
        assertNotEquals(oldMilestone.getId(), milestone.getId());
        assertEquals(FILE2, milestone.getFile());
        //Count up milestonenumber
        assertEquals(3, milestone.getMilestone());
        assertTrue(milestone.isLatestMilestone());

        //Flag the old milestone as deprecated
        assertFalse(oldMilestone.isLatestMilestone());
        assertNotEquals(oldMilestone.getUpdatedDate(), milestone.getUpdatedDate());

        return milestone;
    }

    @Transactional
    public ArtifactMilestoneTO getMilestone(final ArtifactMilestoneTO createdMilestone) {
        final ResponseEntity<ArtifactMilestoneTO> response = this.milestoneController.getMilestone(createdMilestone.getArtifactId(), createdMilestone.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    @Transactional
    public ArtifactMilestoneTO updateMilestone(final String milestoneId, final String artifactId) {
        final ArtifactMilestoneUpdateTO milestoneUpdateTO = MilestoneBuilder.buildMilestoneUpdateTO(milestoneId, "", FILEUPDATE);
        final ResponseEntity<ArtifactTO> lockingResponse = this.artifactController.lockArtifact(artifactId);
        final ResponseEntity<ArtifactMilestoneTO> response = this.milestoneController.updateMilestone(milestoneUpdateTO);
        final ArtifactMilestoneTO milestone = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, milestone.getMilestone());
        assertEquals(FILEUPDATE, milestone.getFile());
        assertEquals("", milestone.getComment());
        assertEquals(milestoneId, milestone.getId());

        return milestone;
    }

    @Transactional
    public void deleteArtifact(final String artifactId, final String firstMilestoneId, final String secondMilestoneId) {
        final ResponseEntity<Void> response = this.artifactController.deleteArtifact(artifactId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThrows(ObjectNotFoundException.class, () -> this.artifactController.getArtifact(artifactId));
        assertThrows(ObjectNotFoundException.class, () -> this.milestoneController.getMilestone(artifactId, firstMilestoneId));
        assertThrows(ObjectNotFoundException.class, () -> this.milestoneController.getMilestone(artifactId, secondMilestoneId));
    }


}
