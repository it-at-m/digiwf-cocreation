package de.muenchen.oss.digiwf.cocreation.core.artifact.api.mapper;


import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.Artifact;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactUpdate;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactUpdateTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.NewArtifactTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ArtifactApiMapper {

    Artifact mapToModel(final NewArtifactTO to);

    ArtifactTO mapToTO(final Artifact model);

    List<ArtifactTO> mapToTO(final List<Artifact> list);

    ArtifactUpdate mapUpdateToModel(final ArtifactUpdateTO to);

}
