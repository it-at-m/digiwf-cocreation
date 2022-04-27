package io.miragon.bpmrepo.core;

import io.miragon.bpmrepo.core.artifact.api.transport.ArtifactTypeTO;
import io.miragon.bpmrepo.core.artifact.plugin.ArtifactTypesPlugin;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * To define new FileTypes, add a new ArtifactTypeTO by providing:
 * - any name that identifies the file type
 * - the file extension
 * - a string which identifies a Material UI-Icon. In order to find available Icons, visit https://mui.com/components/material-icons/
 * - a string which represents the URI of the tool, which is used to edit the corresponding filetype
 */

@Component
public class DefaultFileTypesPlugin implements ArtifactTypesPlugin {

    @Override
    public List<ArtifactTypeTO> getArtifactTypes() {
        final List<ArtifactTypeTO> fileTypes = new ArrayList<>();

        ArtifactTypeTO type = new ArtifactTypeTO("BPMN", "bpmn", "settings", "modeler", true, true);
        fileTypes.add(type);

        type = new ArtifactTypeTO("DMN", "dmn", "view_list", "modeler",true, true);
        fileTypes.add(type);

        type = new ArtifactTypeTO("FORM", "json", "reorder", "formulare",true, true);
        fileTypes.add(type);

        type = new ArtifactTypeTO("CONFIGURATION", "json", "code", null, false, true);
        fileTypes.add(type);

        return fileTypes;
    }
}
