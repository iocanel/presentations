## Having fun with Quarkus
### on Kubernetes and Openshift

---

### about me 

- Sr. Principal Software Engineer @ Red Hat
 - Member of Apache Software Foundation

 - Fabric8 Kubernetes client
 - Spring Cloud Kubernetes
 - Dekorate
 - Quarkus Kubernetes ecosystem

---

### agenda

- Create a small application
- Move it to the cloud
  - Kubernetes
  - OpenShift

---

### demo

---

### creating a quarkus app

- https://code.quarkus.io
- [Quarkus CLI](https://quarkus.io/guides/cli-tooling)

---

### creating a quarkus app using the CLI

```sh
quarkus create app hello-zurich
cd hello-zurich
quarkus dev
```
---

### hello zurich

-  http://localhost:8080/hello

![hello-zurich](/src/hello-zurich.png "hello zurich")
---

### steps to the cloud

- create container image
- create deployment manifests

---

### container images

- Quarkus extensions 
  - quarkus-container-image-docker 
  - quarkus-container-image-buildpacks 
  - quarkus-container-image-jib 
  - quarkus-container-image-openshift

---

### manifest generation

- Quarkus extensions 
  - quarkus-kubernetes
    - quarkus-minikube
    - quarkus-kind
  - quarkus-openshift

---

### build a container image

```sh
quarkus ext add quarkus-container-image-docker
quarkus build -Dquarkus.container-image.build=true
```

or 

```
quarkus image build docker
```

---

### deploying to Kubernetes

```sh
quarkus ext add quarkus-kubernetes
quarkus build -Dquarkus.kubernetes.deploy=true
```

or 

```
quarkus deploy kubernetes
```

---

### My kind cluster doesn't see the image

- Solutions
  - push to public registry
  - push to kind registry
  - use the kind binary
  - use the quarkus-kind extension

```sh
quarkus ext add quarkus-kind
quarkus build -Dquarkus.kubernetes.deploy=true
```

or 

```sh
quarkus deploy kind --image-build
```
---

### moving the app to OpenShift

![openshift](/src/openshift.png "openshift")
- Red Hat distribution of Kubernetes

---

### Openshift Sandbox

![openshift sandbox](/src/openshift-sandbox.webp "openshift sandbox")

- https://developers.redhat.com/developer-sandbox
  - easiest way to get a glimpse of OpenShift
  - free
  - hosted

---

### deploying to Openshift

```sh
quarkus ext add openshift
quarkus build -Dquarkus.openshift.deploy=true
```

or 

```sh
quarkus deploy openshift --image-build
```
---


### Exposing the app


```sh
quarkus deploy openshift --image-build -Dquarkus.openshift.route.expose=true
```

---

### Customizing the manifests

- Add to the application.properties

```sh
quarkus.openshift.lables=FOO=BAR
quarkus.openshift.annotaitons=BAR=BAZ
quarkus.openshift.env.vars.MY_KEY=MY_VALUE

```

---

### Other options

 - See https://quarkus.io/guides/all-config

---

### Integration with other extensions

- Manifests are not just rendered templates
- Tight integration with:
  - kubernetes-config
  - kubernetes-client
  - flyway, liquibase 
  - grpc

---

### Kubernetes Client

- Client needs to talk to API server
  - Requires RBC
    - automatically generated
    - grant the view role

---

### Kubernetes Config

- Client neds to read ConfigMap and Secret
  - Requires RBC
    - automatically generated
    - grant the view role

---

### Initialization Tasks

- Some tasks have different scaling requirements
  - Examples:
    - Database initialization
    - Database migration

- Liquibase, Flyway, etc

---

### Initialization Tasks pt 2

- Externalize init tasks as Jobs

---

### Flyway example

```sh
quarkus ext add quarkus-flyway
```

---

### Flyway Job

```yml
apiVersion: batch/v1
kind: Job
metadata:
  name: hello-zurich
spec:
  completionMode: NonIndexed
  template:
    metadata: {}
    spec:
      containers:
        - env:
            - name: QUARKUS_INIT_AND_EXIT
              value: "true"
            - name: QUARKUS_FLYWAY_ENABLED
              value: "true"
          image: iocanel/hello-zurich:1.0.0-SNAPSHOT
          name: hello-zurich-flyway-init
      restartPolicy: OnFailure
```

---

### questions?

---


### thank you

