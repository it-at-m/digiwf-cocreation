package de.muenchen.oss.digiwf.cocreation.core.sharing;


import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import de.muenchen.oss.digiwf.cocreation.core.sharing.domain.model.ShareWithRepository;
import de.muenchen.oss.digiwf.cocreation.core.sharing.domain.service.ShareService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SharingServiceTest {

    @Autowired
    private ShareService shareService;


    private static final String ARTIFACTID = "123";
    private static final String ARTIFACTID2 = "456";
    private static final String REPOSITORYID = "1213";
    private static final String REPOSITORYID3 = "1415";
    private static final String REPOSITORYID4 = "1516";
    private static final RoleEnum VIEWERROLE = RoleEnum.VIEWER;
    private static final RoleEnum MEMBERROLE = RoleEnum.MEMBER;

    private final ShareWithRepository shareWithRepository = SharingBuilder.buildShareWithRepository(ARTIFACTID, REPOSITORYID, VIEWERROLE);
    private final ShareWithRepository shareWithRepository2 = SharingBuilder.buildShareWithRepository(ARTIFACTID2, REPOSITORYID, VIEWERROLE);
    private final ShareWithRepository shareWithRepository3 = SharingBuilder.buildShareWithRepository(ARTIFACTID2, REPOSITORYID3, VIEWERROLE);
    private final ShareWithRepository shareWithRepository4 = SharingBuilder.buildShareWithRepository(ARTIFACTID2, REPOSITORYID4, VIEWERROLE);


    @Test
    public void tests() {
        this.create();
        this.getAllForRepo();
        this.getAllForArtifact();
        this.update();
    }


    @Transactional
    public void create() {
        final ShareWithRepository shareWithRepository = this.shareService.shareWithRepository(this.shareWithRepository);
        final ShareWithRepository shareWithRepository2 = this.shareService.shareWithRepository(this.shareWithRepository2);
        final ShareWithRepository shareWithRepository3 = this.shareService.shareWithRepository(this.shareWithRepository3);
        final ShareWithRepository shareWithRepository4 = this.shareService.shareWithRepository(this.shareWithRepository4);
        assertNotNull(shareWithRepository);
        assertNotNull(shareWithRepository2);
        assertNotNull(shareWithRepository3);
        assertNotNull(shareWithRepository4);
        assertEquals(ARTIFACTID, shareWithRepository.getArtifactId());
        assertEquals(REPOSITORYID, shareWithRepository.getRepositoryId());
        assertEquals(VIEWERROLE, shareWithRepository.getRole());
    }

    @Transactional
    public void getAllForRepo() {
        final List<ShareWithRepository> shareWithRepositoryList = this.shareService.getSharedArtifactsFromRepository(REPOSITORYID);
        assertEquals(2, shareWithRepositoryList.size());
    }

    @Transactional
    public void getAllForArtifact() {
        final List<ShareWithRepository> shareWithRepositoryList = this.shareService.getSharedRepositories(ARTIFACTID2);
        assertEquals(3, shareWithRepositoryList.size());
    }

    @Transactional
    public void update() {
        this.shareWithRepository.setRole(MEMBERROLE);
        final ShareWithRepository shareWithRepositoryUpdated = this.shareService.updateShareWithRepository(this.shareWithRepository);
        assertNotNull(shareWithRepositoryUpdated);
        assertEquals(MEMBERROLE, shareWithRepositoryUpdated.getRole());
    }

    @Transactional
    @Test
    public void delete() {
        this.shareService.shareWithRepository(this.shareWithRepository);
        this.shareService.shareWithRepository(this.shareWithRepository2);
        this.shareService.shareWithRepository(this.shareWithRepository3);
        this.shareService.shareWithRepository(this.shareWithRepository4);
        final List<ShareWithRepository> shareWithRepositoryList = this.shareService.getSharedRepositories(ARTIFACTID2);
        assertEquals(3, shareWithRepositoryList.size());
        this.shareService.deleteShareWithRepository(ARTIFACTID2, REPOSITORYID);
        final List<ShareWithRepository> shareWithRepositoryList2 = this.shareService.getSharedRepositories(ARTIFACTID2);
        assertEquals(2, shareWithRepositoryList2.size());
    }
}
