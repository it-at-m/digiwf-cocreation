package io.miragon.bpmrepo.core.artifact.api.plugin;

import io.miragon.bpmrepo.core.artifact.api.transport.FileTypesTO;

import java.util.List;

public interface FileTypesPlugin {

    List<FileTypesTO> getFileTypes();

}
