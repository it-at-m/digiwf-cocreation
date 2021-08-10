package io.miragon.bpmrepo.spring.boot.starter.fileTypes;

import io.miragon.bpmrepo.core.artifact.api.plugin.FileTypesPlugin;
import io.miragon.bpmrepo.core.artifact.api.transport.FileTypesTO;

import java.util.ArrayList;
import java.util.List;

public class DefaultFileTypesPlugin implements FileTypesPlugin {

    @Override
    public List<FileTypesTO> getFileTypes() {
        final List<FileTypesTO> fileTypes = new ArrayList<>();

        FileTypesTO type = new FileTypesTO("BPMN", "bpmn", "settings", "modeler");
        fileTypes.add(type);

        type = new FileTypesTO("DMN", "dmn", "view_list", "modeler");
        fileTypes.add(type);

        type = new FileTypesTO("FORM", "docx", "reorder", "formulare");
        fileTypes.add(type);

        type = new FileTypesTO("CONFIGURATION", "txt", "code", "konfiguration");
        fileTypes.add(type);

        return fileTypes;
    }
}
