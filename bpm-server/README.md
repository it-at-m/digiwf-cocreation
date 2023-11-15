# BPM-Server

## Getting Started

````bash
# build
mvn clean install
# start the application
mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=local, no-mail, no-security, streaming, testdata"
````

Useful profiles for development:

* **local**: Settings for local development
* **no-mail**: Disable email messaging
* **no-security**: Disable spring security
* **streaming**: Enable event streaming to kafka cluster
* **testdata**: Initialize database with testdata from *resources/db/testdata*


## Create new Artefact
1. Use Swagger-UI: http://localhost:8092/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/Artifact/createArtifact
2. Create new Repository: http://localhost:8092/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/Repository/createRepository 
   and remember id
3. Create new Artefact: http://localhost:8092/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/Artifact/createArtifact 
   and use repo-ID from 2.
   Use this payload:
```
{
   "name": "Mein Formular",
   "description": "Das ist mein Formular",
   "fileType": "FORM",
   "file": ""
   } 
```
  With the file left empty the form-editor will create a default form.