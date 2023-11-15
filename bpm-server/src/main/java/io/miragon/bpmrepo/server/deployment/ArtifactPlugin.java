package io.miragon.bpmrepo.server.deployment;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTypeTO;
import io.miragon.bpmrepo.core.artifact.plugin.ArtifactTypesPlugin;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ArtifactPlugin implements ArtifactTypesPlugin {

    @Override
    public List<ArtifactTypeTO> getArtifactTypes() {
        final List<ArtifactTypeTO> artifactTypes = new ArrayList<>();

        ArtifactTypeTO type = new ArtifactTypeTO("BPMN", "bpmn", "settings", "modeler",true, true);
        artifactTypes.add(type);

        type = new ArtifactTypeTO("DMN", "dmn", "view_list", "modeler", true, true);
        artifactTypes.add(type);

        type = new ArtifactTypeTO("FORM", "json", "reorder", "forms", true, true);
        artifactTypes.add(type);

        type = new ArtifactTypeTO("ELEMENT_TEMPLATE", "json", "content_copy", null, false, false);
        artifactTypes.add(type);

        type = new ArtifactTypeTO("CONFIGURATION", "json", "editor", null, false, true);
        artifactTypes.add(type);

//        type = new ArtifactTypeTO("BUILDINGBLOCK", "json", "widgets", "editor");
//        artifactTypes.add(type);

        return artifactTypes;
    }
}
