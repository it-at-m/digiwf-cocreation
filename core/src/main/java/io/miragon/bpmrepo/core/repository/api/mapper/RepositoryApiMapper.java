package io.miragon.bpmrepo.core.repository.api.mapper;

import io.miragon.bpmrepo.core.repository.api.transport.NewRepositoryTO;
import io.miragon.bpmrepo.core.repository.api.transport.RepositoryTO;
import io.miragon.bpmrepo.core.repository.api.transport.RepositoryUpdateTO;
import io.miragon.bpmrepo.core.repository.domain.model.NewRepository;
import io.miragon.bpmrepo.core.repository.domain.model.Repository;
import io.miragon.bpmrepo.core.repository.domain.model.RepositoryUpdate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RepositoryApiMapper {

    NewRepository mapToModel(final NewRepositoryTO to);

    RepositoryUpdate mapToModel(final RepositoryUpdateTO to);

    RepositoryTO mapToTO(final Repository repository);

    List<RepositoryTO> mapToTO(final List<Repository> list);

}
