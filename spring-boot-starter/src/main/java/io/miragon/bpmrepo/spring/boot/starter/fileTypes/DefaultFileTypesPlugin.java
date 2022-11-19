package io.miragon.bpmrepo.spring.boot.starter.fileTypes;


import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTypeTO;
import io.miragon.bpmrepo.core.artifact.plugin.ArtifactTypesPlugin;

import java.util.ArrayList;
import java.util.List;

public class DefaultFileTypesPlugin implements ArtifactTypesPlugin {

    @Override
    public List<ArtifactTypeTO> getArtifactTypes() {
        final List<ArtifactTypeTO> fileTypes = new ArrayList<>();

        ArtifactTypeTO type = new ArtifactTypeTO("BPMN", "bpmn", "settings", "modeler", true, true);
        fileTypes.add(type);

        type = new ArtifactTypeTO("DMN", "dmn", "view_list", "modeler", true, true);
        fileTypes.add(type);

        type = new ArtifactTypeTO("FORM", "json", "reorder", "formulare", true, true);
        fileTypes.add(type);

        type = new ArtifactTypeTO("ELEMENT_TEMPLATE", "json", "content_copy", null, false, false);
        fileTypes.add(type);

        type = new ArtifactTypeTO("CONFIGURATION", "json", "code", null, false, true);
        fileTypes.add(type);

        return fileTypes;
    }
}
