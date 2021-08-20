package io.miragon.bpmrepo.core.artifact.plugin;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTypeTO;

import java.util.List;

public interface ArtifactTypesPlugin {

    List<ArtifactTypeTO> getArtifactTypes();

}
