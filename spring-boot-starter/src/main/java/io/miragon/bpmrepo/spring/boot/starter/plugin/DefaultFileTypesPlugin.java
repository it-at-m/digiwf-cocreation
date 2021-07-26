package io.miragon.bpmrepo.spring.boot.starter.plugin;

import io.miragon.bpmrepo.core.artifact.api.plugin.FileTypesPlugin;
import io.miragon.bpmrepo.core.artifact.api.transport.FileTypesTO;

import java.util.ArrayList;
import java.util.List;

public class DefaultFileTypesPlugin implements FileTypesPlugin {

    @Override
    public List<FileTypesTO> getFileTypes() {
        final List<FileTypesTO> fileTypes = new ArrayList<>();

        FileTypesTO type = new FileTypesTO("BPMN", "settings");
        fileTypes.add(type);

        type = new FileTypesTO("DMN", "view_list");
        fileTypes.add(type);

        type = new FileTypesTO("FORM", "reorder");
        fileTypes.add(type);

        type = new FileTypesTO("CONFIGURATION", "code");
        fileTypes.add(type);

        return fileTypes;
    }
}
