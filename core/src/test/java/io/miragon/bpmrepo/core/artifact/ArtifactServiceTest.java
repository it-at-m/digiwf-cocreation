package io.miragon.bpmrepo.core.artifact;


import io.miragon.bpmrepo.core.artifact.domain.exception.LockedException;
import io.miragon.bpmrepo.core.artifact.domain.mapper.ArtifactMapper;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactUpdate;
import io.miragon.bpmrepo.core.artifact.domain.service.ArtifactService;
import io.miragon.bpmrepo.core.artifact.domain.service.LockService;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.ArtifactEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.ArtifactJpaRepository;
import io.miragon.bpmrepo.core.user.UserBuilder;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ArtifactServiceTest {

    @Autowired
    private ArtifactJpaRepository jpaRepository;

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    private ArtifactMapper artifactMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private LockService lockService;


    private static final String ARTIFACTID = "123456";
    private static final String REPOID = "01";
    private static final String ARTIFACTNAME = "TestArtifact";
    private static final String ARTIFACTNAMEUPDATE = "UPDATENAME";
    private static final String ARTIFACTDESC = "SomeDescription";
    private static final String ARTIFACTDESCUPDATE = "UPDATEDESCRIPTION";
    private static final String FILE_TYPE = "BPMN";

    private static final String USERNAME = "USER";
    private static final String USERID = "123";
    private static final String USERNAME2 = "USER2";
    private static final String USERID2 = "456";
    private static final LocalDateTime DATE = LocalDateTime.now();


    @Test
    public void tests() {
        final Artifact artifact = this.create();
        final Artifact queriedArtifact = this.get(artifact);
        final Artifact updatedArtifact = this.update(queriedArtifact);
        this.delete(updatedArtifact);
    }

    public Artifact create() {
        final Artifact artifact = ArtifactBuilder.buildArtifact(ARTIFACTID, REPOID, ARTIFACTNAME, ARTIFACTDESC, FILE_TYPE, DATE, DATE);
        final Artifact createdArtifact = this.artifactService.createArtifact(artifact);
        assertNotNull(createdArtifact);
        return createdArtifact;
    }

    public Artifact get(final Artifact artifact) {
        final Optional<Artifact> queriedArtifact = this.artifactService.getArtifactById(artifact.getId());
        assertNotNull(queriedArtifact.get());
        assertEquals(artifact.getName(), queriedArtifact.get().getName());
        return queriedArtifact.get();
    }

    public Artifact update(final Artifact queriedArtifact) {
        final ArtifactUpdate artifactUpdate = ArtifactBuilder.buildArtifactUpdate(ARTIFACTNAMEUPDATE, ARTIFACTDESCUPDATE);
        final Artifact updatedArtifact = this.artifactService.updateArtifact(queriedArtifact, artifactUpdate);
        assertNotNull(updatedArtifact);
        assertEquals(artifactUpdate.getName(), updatedArtifact.getName());
        assertEquals(artifactUpdate.getDescription(), updatedArtifact.getDescription());
        return updatedArtifact;
    }

    public void delete(final Artifact updatedArtifact) {
        this.artifactService.deleteArtifact(updatedArtifact.getId());
        final Optional<Artifact> deletedArtifact = this.artifactService.getArtifactById(updatedArtifact.getId());
        assertTrue(deletedArtifact.isEmpty());
    }


    @Test
    @Transactional
    public void countArtifactsAndDeleteAllByRepositoryId() {
        //Create two Artifacts in the same repository
        final ArtifactEntity artifactEntity = ArtifactBuilder.buildArtifactEntity(ARTIFACTID, REPOID, ARTIFACTNAME, ARTIFACTDESC, DATE, DATE);
        final ArtifactEntity artifactEntity2 = ArtifactBuilder.buildArtifactEntity(ARTIFACTID, REPOID, ARTIFACTNAME, ARTIFACTDESC, DATE, DATE);
        this.jpaRepository.save(artifactEntity);
        this.jpaRepository.save(artifactEntity2);

        //Count if repository contains two artifacts
        final Integer existingArtifacts = this.artifactService.countExistingArtifacts(REPOID);
        assertEquals(2, existingArtifacts);

        //Delete all contained artifacts
        final int deletedEntities = this.jpaRepository.deleteAllByRepositoryId(REPOID);
        assertEquals(2, deletedEntities);
    }


    @Test
    @Transactional
    public void lockAndUnlock() {
        final User user = UserBuilder.buildUser(USERID, USERNAME);
        final User user2 = UserBuilder.buildUser(USERID2, USERNAME2);
        //Create an artifact
        final Artifact artifact = ArtifactBuilder.buildArtifact(ARTIFACTID, REPOID, ARTIFACTNAME, ARTIFACTDESC, FILE_TYPE, DATE, DATE);
        final Artifact createdArtifact = this.artifactService.createArtifact(artifact);
        assertNull(createdArtifact.getLockedBy());
        assertNull(createdArtifact.getLockedUntil());

        //lock it
        final Artifact lockedArtifact = this.artifactService.lockArtifact(createdArtifact.getId(), USERNAME);
        assertEquals(USERNAME, lockedArtifact.getLockedBy());
        assertTrue(lockedArtifact.getLockedUntil().isAfter(DATE));

        //pass the lock service check
        when(this.userService.getCurrentUser()).thenReturn(user);
        this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(lockedArtifact);

        //check if noone else can access the artifact
        when(this.userService.getCurrentUser()).thenReturn(user2);
        assertThrows(LockedException.class, () -> this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(lockedArtifact));

        final Artifact unlockedArtifact = this.artifactService.unlockArtifact(lockedArtifact.getId());
        this.lockService.checkIfMilestoneIsUnlockedOrLockedByActiveUser(unlockedArtifact);
    }

}
