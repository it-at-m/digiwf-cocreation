package io.miragon.bpmrepo.core.artifact.api.mapper;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactVersionTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactVersionUpdateTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactVersionUploadTO;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersion;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersionUpdate;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactVersionUpload;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ArtifactVersionApiMapper {

    ArtifactVersionUpload mapUploadToModel(final ArtifactVersionUploadTO to);

    ArtifactVersionUpdate mapUpdateToModel(final ArtifactVersionUpdateTO to);

    ArtifactVersionTO mapToTO(final ArtifactVersion model);

    List<ArtifactVersionTO> mapToTO(final List<ArtifactVersion> list);

}
