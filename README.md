# DigiWF-CoCreation

*DigiWF-CoCreation is a process development environment tailored to the [DigiWF](https://github.com/it-at-m/digiwf-core) workflow automation and integration platform*

### Built With

The documentation project is built with technologies we use in our projects:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Cloud](https://spring.io/projects/spring-cloud)

## Set up

1. Start the bpm-repo-client and API-Gateway
```bash
docker compose --profile bpm-repo-client up
```
2. Start the bpm-repo example or the bpm-server
- bpm-repo example with the profiles local
- bpm-server with the profiles local and no-security
3. Start the [bpm-modeler](https://git.muenchen.de/digitalisierung/bpm-modeler) (frontend)
- `npm run serve`
4. Start the [digiwf-forms](https://git.muenchen.de/digitalisierung/digiwf-forms) (frontend)
- `npm run serve`

## Documentation

## Plugins

### FileTypes

The repository can handle any format you want, you just have to declare the allowed file types by using the fileTypes plugin.
By default, the repository is configured to support these file types and formats:
- BPMN Diagrams (.bpmn)
- DMN Diagrams (.dmn)
- Forms (.json)
- Configurations (.json)

In order to define a new file type, open the CustomFileTypesPlugin and add a new ArtifactTypeTO.
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

If you want to adjust the targets, navigate to CustomDeploymentPlugin and simply add any string to *deploymentTargets*.

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

#### Deployment Status

The status of a deployment can be *PENDING*, *SUCCESS* or *ERROR*. Every deployment has the status *PENDING* until you change it.

To update the status of a deployment you can implement a `DeploymentAdapterImpl` that calls the `updateDeployment(...)` Method
on the `DeploymentAdapter` base calls.

```java
@Component
public class DeploymentAdapterImpl extends DeploymentAdapter {

    public Deployment successfulDeployment(final String deploymentId) {
        return this.updateDeployment(deploymentId, DeploymentStatus.SUCCESS, "Deployment was successful");
    }

    public Deployment failedDeployment(final String deploymentId) {
        return this.updateDeployment(deploymentId, DeploymentStatus.ERROR, "Deployment failed");
    }

}
```

The test below shows an example usage of the deployment adapter.

```java
@Test
void deploymentAdapter() {
        Deployment updatedDeployment = this.deploymentAdapter.successfulDeployment(this.deployment.getId());
        Assertions.assertEquals(DeploymentStatus.SUCCESS, updatedDeployment.getStatus());
        Assertions.assertNotNull(updatedDeployment.getMessage());

        updatedDeployment = this.deploymentAdapter.failedDeployment(this.deployment.getId());
        Assertions.assertEquals(DeploymentStatus.ERROR, updatedDeployment.getStatus());
        Assertions.assertNotNull(updatedDeployment.getMessage());
}
```

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please open an issue with the tag "enhancement", fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Open an issue with the tag "enhancement"
2. Fork the Project
3. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
4. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
5. Push to the Branch (`git push origin feature/AmazingFeature`)
6. Open a Pull Request

More about this in the [CODE_OF_CONDUCT](/CODE_OF_CONDUCT.md) file.

## License

Distributed under the Apache Version 2.0 License. See [LICENSE](LICENSE) file for more information.

## Contact

it@M - opensource@muenchen.de
