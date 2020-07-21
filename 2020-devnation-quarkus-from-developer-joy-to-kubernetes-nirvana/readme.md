# Quarkus: from developer joy to Kubernetes nirvana

https://www.youtube.com/watch?v=PkhyvHE1PTQ

## Create a new Quarkus application

Create an new Quarkus application:

```
    mvn io.quarkus:quarkus-maven-plugin:1.6.0.Final:create -DprojectGroupId=org.acme -DprojectArtifactId=hello-world -DprojectVersion=0.1-SNAPSHOT -Dendpoint=/hello -DclassName=org.acme.Hello
```

## Run the application in develper mode

```
mvn clean package quarkus:dev
```

Show `Hello.java`, the `/hello` endpoint in the browser and demonstrate how changing the output message takes effect in the browser.

## Add the container image docker extension

Either open `pom.xml` and add the dependency: `io.quarkus:quarkus-container-image-docker` or using the cli:

```
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-container-image-docker"
```

## Perform a container image build

```
mvn clean package -Dquarkus.container-image.build=true
```

## Configure the container image extension to always trigger a build

Open `src/main/resources/application.properties and add:

```
quarkus.container-image.build=true
```

then demonstrate how the container build is automatically triggered:

```
mvn clean install
```

## Configure the extension to always perform a build & push

Substitute:
```
quarkus.container-image.build=true
```

with:
```
quarkus.container-image.push=true
```

## Add registry configuration

```
quarkus.container-image.registry=docker.io
quarkus.container-image.username=iocanel
quarkus.container-image.password=<my dockerhub password>
```

Explain that username & password are optional and that authentication infromation can be automatically retrieved from `.docker/config.json`.

## Switch from docker to jib

Open `pom.xml` change the artifactId `quarkus-container-image-docker` to `quarkus-container-image-jib`.
Demonstrate that build now works with jib:

```
mvn clean install
```

Once done, switch back to docker (we will need it later: It's somehow faster to use docker with minikube).
Also, to speed things up, let us not `push` each time we perform a maven build: Remove `quarkus.container-image.push=true` from `application.properties`

## Add the kubernetes extension

Either open `pom.xml` and add the dependency: `io.quarkus:quarkus-kubernetes` or using the cli:

```
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-kubernetes"
```

## Perform a build and show generated manifests

```
mvn clean install
```

Open `target/kubernetes/kubernetes.yml` 

and show generated manifests. Show the image used to demonstrate the synergy between kubernetes and container-image-xxx extensions.

## Customize the generated manifests

*Note*: Developer mode, will *NOT* automatically regenerate kubernetes manifests, not until  https://github.com/quarkusio/quarkus/pull/10805 is merged.
So, either an `mvn clean package` is required to demonstrate the customizations below, or a custom quarkus build that includes #10805, or an editor hack to automatically trigger `mvn clean package` behind the scene. In the live presentation the later was used.

### Add labels

Add the line below in the `application.properties` 

```
quarkus.kubernetes.labels.FOO=BAR
```

### Add annotations
Add the line below in the `application.properties` 

```
quarkus.annotations.labels.FOO=BAR
```

### Add environment variables
Add the line below in the `application.properties` 

```
quarkus.annotations.env-vars.DB_USER.value=iocanel
```

Expalin how evn-vars are different from lables and annotations (e.g. field ref, secrets/configmap property refs).

#### Add secret property mapping
Add an environment variable pointing to a secret property

```
quarkus.kubernetes.env.mapping.DB_PASSWORD.from-secret=db-secret
quarkus.kubernetes.env.mapping.DB_PASSWORD.with-key=database.password
```

## Demonstrate Bring Your Own Resources

Create `src/main/kubernetes/kubernetes.yml`

Add in there a custom `Deployment`.

For the demo I used the generated `Deployment` as an input, stripped of things like:
- image
- service account
- ports

And also added by hand additional labels (e.g. `created-by=hand`).

Also stripped `application.properties` from environment variable configuration (to demonstrate how it will be picked up from the input `Deployment`).

Rebuild:

```
mvn clean install
```

Show generated `Deployment` and explain how it combines elements from:

- The provided Deployment (the one in src/main/kubernetes/kubernetes.yml).
- The `application.properties`
- The Quarkus build system
  - Ports
  - Image
  - Probes (requires you to also add `quarkus-smallrye-health` in the `pom.xml`)

*Note*: It's easy to screw up labels and selectors this way, so its safer to delete `src/main/kubernets` before moving the next step.

## Deploy to minikube

It should be possible to apply the generated manifests as is. However, its an excellent time to talk about the quarkus minikube extension.

Either open `pom.xml` and add the dependency: `io.quarkus:quarkus-minikube` or using the cli:

```
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-minikube"
```

*Note*: The minikube extension may replace `quarkus-kubernetes` or added next to it. It doesn't really matter.


```
mvn clean package -Dquarkus.kubernetes.deploy=true
```

### Perform a change in the message and redeploy

To show how fast the process becomes with minikube (skipping pushes etc) let's change `Hello.java` again and redeploy:

```
mvn clean package -Dquarkus.kubernetes.deploy=true
```

#### Get the service url

```
minikube service list
```

Use the listed url to demonstrate the updated message.

## Demonstrate openshift

Either open `pom.xml` and add the dependency: `io.quarkus:quarkus-openshift` or using the cli:

```
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-openshfit"
```

Once again, the `quarkus-openshift` may or may not replace the existing kubernetes extensions.

Build the project:

```
mvn clean package
```

Show the generated manifests under `target/kubernetes/openshift.yml`.

*Note* From now on you'll need to login to an openshift cluster:

```
oc login ....
```

### Demonstrate an S2i build

Just remove `io.quarkus:quarkus-container-image-docker` from `pom.xml`

No, need to add `io.quarkus:quarkus-container-image-s2i` as its brough in by `quarkus-openshift`.

We are ready to build and deploy:

```
mvn clean package -Dquarkus.kubernetes.deploy=true
```

If we wanted to just demonstrate just the s2i build part:

```
mvn clean pacakge -Dquarkus.container-image.build=true
```

### Expose the route automatically

Add the following property to `application.properties`

```
quarkus.openshift.expose=true
```

After rebuilding show in `openshift.yml` the generated `Route`.


## Customize Openshift manifests

Search & Replace all `kubernetes` to `openshift` in `application.properties` and show how all customizations demonstrated for kubernetes also apply in Openshift.

## Show native integration

Explain how everything demonstrated also applies in native mode, demonstrate Openshift + s2i + native:

```
mvn clean install -Dquarkus.kubernetes.deploy=true -Pnative
```
