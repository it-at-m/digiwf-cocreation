package io.miragon.bpmrepo.core.artifact.api.mapper;

import io.miragon.bpmrepo.core.artifact.api.transport.ShareWithRepositoryTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ShareWithTeamTO;
import io.miragon.bpmrepo.core.artifact.domain.model.ShareWithRepository;
import io.miragon.bpmrepo.core.artifact.domain.model.ShareWithTeam;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SharedApiMapper {

    ShareWithRepositoryTO mapToShareRepoTO(final ShareWithRepository shared);

    ShareWithRepository mapToShareRepoModel(final ShareWithRepositoryTO to);

    ShareWithTeam mapToShareTeamModel(final ShareWithTeamTO to);

    ShareWithTeamTO mapToShareTeamTO(final ShareWithTeam shared);

    List<ShareWithRepositoryTO> mapToShareRepoTO(final List<ShareWithRepository> list);

    List<ShareWithTeamTO> mapToShareTeamTO(final List<ShareWithTeam> list);
}
