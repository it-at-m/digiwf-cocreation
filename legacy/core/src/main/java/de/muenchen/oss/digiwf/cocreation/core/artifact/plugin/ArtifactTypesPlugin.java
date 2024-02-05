package de.muenchen.oss.digiwf.cocreation.core.artifact.plugin;

import de.muenchen.oss.digiwf.cocreation.core.artifact.api.transport.ArtifactTypeTO;

import java.util.List;


/**
 * To define new FileTypes, add a new ArtifactTypeTO by providing:
 * - any name that identifies the file type
 * - the file extension
 * - a string which identifies a Material UI-Icon. In order to find available Icons, visit https://mui.com/components/material-icons/
 * - a string which represents the URI of the tool, which is used to edit the corresponding filetype
 */


public interface ArtifactTypesPlugin {

    List<ArtifactTypeTO> getArtifactTypes();

}
