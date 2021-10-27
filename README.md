# BPM Repository

Check out the [documentation](https://www.flowsquad.io/) for further information.


## Plugins

### FileTypes

The repository can handle any format you want, you just have to declare the allowed file types by using the fileTypes plugin.
By default, the repository is configured to support these file types and formats:
- BPMN Diagrams (.bpmn)
- DMN Diagrams (.dmn)
- Forms (.json)
- Configurations (.json)

In order to define a new file type, open the [CustomFileTypesPlugin](src/main/java/io/miragon/bpmrepo/server/CustomFileTypesPlugin.java) and add a new ArtifactTypeTO.
It takes four arguments to create a new ArtifactTypeTO:
1. Any name which will describe the file type in the UI
2. The file extension
3. A string which identifies a Material UI-Icon. In order to find available Icons, visit https://mui.com/components/material-icons/
4. A string which represents the URI of the tool, which is used to edit the corresponding filetype (If you don't provide an online tool which can handle the corresponding file type, you can just leave it blank - be aware that clicking on the artifact will then link to an unavailable service)

```java
@Component
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
```


### Deployment

In order to define what happens when a file is ready to review/ ready for production/ etc., you can create your own deployment plugin. You can define the deployment-targets aswell as the business logic that is executed when a file is deployed.

By default, three deployment targets are available:
 - Production
 - Review
 - Management

If you want to adjust the targets, navigate to [CustomDeploymentPlugin](src/main/java/io/miragon/bpmrepo/server/CustomDeploymentPlugin.java) and simply add any string to *deploymentTargets*.

```java
@Component
public class CustomDeploymentPlugin implements DeploymentPlugin {
    
    @Override
    public List<String> getDeploymentTargets() {
        final List<String> deploymentTargets = new ArrayList<>();
        deploymentTargets.add("Produktion");
        deploymentTargets.add("Review");
        deploymentTargets.add("Management");

        return deploymentTargets;
    }
}
```

If you want to customize the business logic that is executed when a file is being deployed, adjust the *deploy* method.
```java
    @Override
    public void deploy(final String versionId, final String target) {
    }
```

# Issues and Questions

If you experience any bugs or have questions concerning the usage or further development plans, don't hesitate to create a new issue. However, **please make sure to include all relevant logs, screenshots, and code examples**. Thanks! 



# License

```
/**
 * Copyright 2021 FlowSquad GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```

For the full license text, see the LICENSE file above.
Remember that the bpmn.io license still applies, i.e. you must not remove the icon displayed in the bottom-right corner.
