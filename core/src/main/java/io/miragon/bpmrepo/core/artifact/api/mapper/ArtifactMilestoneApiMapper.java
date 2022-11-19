package io.miragon.bpmrepo.core.artifact.api.mapper;


import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactMilestoneTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactMilestoneUpdateTO;
import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactMilestoneUploadTO;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestone;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestoneUpdate;
import io.miragon.bpmrepo.core.artifact.domain.model.ArtifactMilestoneUpload;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ArtifactMilestoneApiMapper {

    ArtifactMilestoneUpload mapUploadToModel(final ArtifactMilestoneUploadTO to);

    ArtifactMilestoneUpdate mapUpdateToModel(final ArtifactMilestoneUpdateTO to);

    ArtifactMilestoneTO mapToTO(final ArtifactMilestone model);

    List<ArtifactMilestoneTO> mapToTO(final List<ArtifactMilestone> list);

}
