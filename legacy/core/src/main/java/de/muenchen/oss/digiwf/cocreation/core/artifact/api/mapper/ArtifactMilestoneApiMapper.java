package de.muenchen.oss.digiwf.cocreation.core.artifact.api.mapper;


import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactMilestoneTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactMilestoneUpdateTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactMilestoneUploadTO;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactMilestone;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactMilestoneUpdate;
import de.muenchen.oss.digiwf.cocreation.core.artifact.domain.model.ArtifactMilestoneUpload;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ArtifactMilestoneApiMapper {

    ArtifactMilestoneUpload mapUploadToModel(final ArtifactMilestoneUploadTO to);

    ArtifactMilestoneUpdate mapUpdateToModel(final ArtifactMilestoneUpdateTO to);

    ArtifactMilestoneTO mapToTO(final ArtifactMilestone model);

    List<ArtifactMilestoneTO> mapToTO(final List<ArtifactMilestone> list);

}
