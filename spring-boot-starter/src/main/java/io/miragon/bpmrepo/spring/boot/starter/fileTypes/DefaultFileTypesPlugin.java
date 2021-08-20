package io.miragon.bpmrepo.spring.boot.starter.fileTypes;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTypeTO;
import io.miragon.bpmrepo.core.artifact.plugin.ArtifactTypesPlugin;

import java.util.ArrayList;
import java.util.List;

public class DefaultFileTypesPlugin implements ArtifactTypesPlugin {

    @Override
    public List<ArtifactTypeTO> getArtifactTypes() {
        final List<ArtifactTypeTO> fileTypes = new ArrayList<>();

        ArtifactTypeTO type = new ArtifactTypeTO("BPMN", "bpmn", "settings", "modeler");
        fileTypes.add(type);

        type = new ArtifactTypeTO("DMN", "dmn", "view_list", "modeler");
        fileTypes.add(type);

        type = new ArtifactTypeTO("FORM", "json", "reorder", "formulare");
        fileTypes.add(type);

        type = new ArtifactTypeTO("CONFIGURATION", "json", "code", "konfiguration");
        fileTypes.add(type);

        return fileTypes;
    }
}
