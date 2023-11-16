package de.muenchen.oss.digiwf.cocreation.core.sharing.api.resource;


import de.muenchen.oss.digiwf.cocreation.core.sharing.api.transport.ShareWithRepositoryTO;
import de.muenchen.oss.digiwf.cocreation.core.sharing.domain.model.ShareWithRepository;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SharedApiMapper {

    ShareWithRepositoryTO mapToShareRepoTO(final ShareWithRepository shared);

    ShareWithRepository mapToShareRepoModel(final ShareWithRepositoryTO to);

    List<ShareWithRepositoryTO> mapToShareRepoTO(final List<ShareWithRepository> list);

}
