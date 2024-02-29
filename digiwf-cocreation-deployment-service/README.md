# DigiWF CoCreation Deployment

The __DigiWF CoCreation__ project is a part of the __DigiWF__ project and is used to create and maintain the processes for the [__DigiWF__](https://github.com/it-at-m/digiwf-core) project.
For more information and facts about the development please visit [digiwf.oss.muenchen.de](https://digiwf.oss.muenchen.de)

## Getting started

1. Build the project with `mvn clean install`
2. Run the digiwf-cocreation-deployment with the profile `local` and the environment variables from [../local.env](../local.env) set.

**Testing with miranum-cli**

> You can checkout [resources/ide-showcase](resources/ide-showcase) to test the deployment with the miranum-cli.

If you want to create your own showcase you should set up a new project with *miranum-cli* and edit (or create) a `miranum.json` file with the following content:

```json
{
  "projectVersion": "1.0.0",
  "name": "Showcase-Miranum-IDE",
  "workspace": [
    { "type": "bpmn", "path": "", "extension": ".bpmn" },
    { "type": "dmn", "path": "", "extension": ".dmn" },
    { "type": "form", "path": "forms", "extension": ".form" },
    { "type": "element-template", "path": "element-templates", "extension": ".json" },
    { "type": "config", "path": "configs", "extension": ".config.json" }
  ],
  "deployment": [
    {
      "plugin": "rest",
      "targetEnvironments": [
        {
          "name": "local",
          "url": "http://localhost:9010"
        }
      ]
    }
  ]
}
```

Under `deployment.targetEnvironments` you can define the target environments for the deployment. The `name` is the name of the environment and the `url` is the URL of the digiwf-cocreation-deployment.
For this example the `url` is `http://localhost:9010` which is configured in [../local.env](../local.env).

## Configuration

- **DEPLOYMENT_SERVER_PORT** - The port the server listens on. Default is 8080.
- **SSO_DEPLOYMENT_CLIENT_ID** - The client id for the SSO deployment client.
- **SSO_DEPLOYMENT_CLIENT_SECRET** - The client secret for the SSO deployment client.

**Configure the deployment receivers**

The following example shows the configuration for a *local* deployment receiver in `application-local.yml`.

```yaml
io:
  miranum:
    deploymentserver:
      rest:
        targets:
          local:
            bpmn: '${ENGINE_HOST:http://localhost}:${ENGINE_SERVER_PORT}/rest/deployment/v2/'
            dmn: '${ENGINE_HOST:http://localhost}:${ENGINE_SERVER_PORT}/rest/deployment/v2/'
            form: '${ENGINE_HOST:http://localhost}:${ENGINE_SERVER_PORT}/rest/deployment/v2/'
            config: '${ENGINE_HOST:http://localhost}:${ENGINE_SERVER_PORT}/rest/deployment/v2/'
```

If you want to configure the local deployment receiver, you can set the `ENGINE_HOST` and `ENGINE_SERVER_PORT` environment variables.
- **ENGINE_HOST** - The host of the engine server. Default is `http://localhost`.
- **ENGINE_SERVER_PORT** - The port of the engine server, e.g. `39146`.

If you want to add additional stages or environments, you can define them with environment variables:

```bash
# replace CUSTOMSTAGE with the name of your stage
IO_MIRANUM_DEPLOYMENTSERVER_REST_TARGETS_CUSTOMSTAGE_BPMN="${ENGINE_HOST:http://localhost}:${ENGINE_SERVER_PORT}/rest/deployment/v2/"
IO_MIRANUM_DEPLOYMENTSERVER_REST_TARGETS_CUSTOMSTAGE_DMN="${ENGINE_HOST:http://localhost}:${ENGINE_SERVER_PORT}/rest/deployment/v2/"
IO_MIRANUM_DEPLOYMENTSERVER_REST_TARGETS_CUSTOMSTAGE_FORM="${ENGINE_HOST:http://localhost}:${ENGINE_SERVER_PORT}/rest/deployment/v2/"
IO_MIRANUM_DEPLOYMENTSERVER_REST_TARGETS_CUSTOMSTAGE_CONFIG="${ENGINE_HOST:http://localhost}:${ENGINE_SERVER_PORT}/rest/deployment/v2/"
```
