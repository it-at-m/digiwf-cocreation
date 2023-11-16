package de.muenchen.oss.digiwf.cocreation.core.repository.api.mapper;

import de.muenchen.oss.digiwf.cocreation.core.repository.api.transport.NewRepositoryTO;
import de.muenchen.oss.digiwf.cocreation.core.repository.api.transport.RepositoryTO;
import de.muenchen.oss.digiwf.cocreation.core.repository.api.transport.RepositoryUpdateTO;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.NewRepository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.Repository;
import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.RepositoryUpdate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RepositoryApiMapper {

    NewRepository mapToModel(final NewRepositoryTO to);

    RepositoryUpdate mapToModel(final RepositoryUpdateTO to);

    RepositoryTO mapToTO(final Repository repository);

    List<RepositoryTO> mapToTO(final List<Repository> list);

}
