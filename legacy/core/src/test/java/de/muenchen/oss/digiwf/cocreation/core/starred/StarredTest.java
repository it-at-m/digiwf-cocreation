package de.muenchen.oss.digiwf.cocreation.core.starred;


import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.mapper.StarredMapper;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Starred;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.service.StarredService;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity.StarredEntity;
import de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.repository.StarredJpaRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StarredTest {

    @InjectMocks
    private StarredService starredService;

    @Mock
    private StarredJpaRepository starredJpaRepository;

    @Mock
    private StarredMapper starredMapper;

    @Mock
    private Starred starred;

    @Mock
    private StarredEntity starredEntity;


    private static final String ARTIFACTID = "1234";
    private static final String USERID = "5678";


}
