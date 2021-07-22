package io.miragon.bpmrepo.core.artifact.api.mapper;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactUpdateTO;
import io.miragon.bpmrepo.core.artifact.api.transport.NewArtifactTO;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactUpdate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ArtifactApiMapper {

    Artifact mapToModel(final NewArtifactTO to);

    ArtifactTO mapToTO(final Artifact model);

    List<ArtifactTO> mapToTO(final List<Artifact> list);

    ArtifactUpdate mapUpdateToModel(final ArtifactUpdateTO to);

}
