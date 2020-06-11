# Java2Days Sofia: Get your hands dirty with Quarkus

This repository contains all the code and assets that was written during the presentation.

## Table of contents
 - [Requirements](#Requirements): What you need before we start.
 - [Assets](#Assets): Scripts and manifests to setup services we are going to use.
 - [Walkthrough](#Walkthrough): The steps we are going to follow.
 - [Milestones](#Milestones): Links to snapshots/milestones of this presentation.

## Requirements
To use this material you need to have setup the following:

- Docker & Docker compose (local docker daemon)
  - Images (for a smoother experience, please get the images prior to the lab): 
    - debian:jessie-slim
    - postgres:10.5
    - fabric8/java-alpine-openjdk8-jre
    - registry.fedoraproject.org/fedora-minimal
    - strimzi/kafka:0.11.3-kafka-2.1.0
    - fabric8/java-centos-openjdk8-jdk

- Java 1.8+
- Maven (3.5.3+)
- Graal (used GraalVM CE 19.2.1)

### Nice to have

The following are optional requirements, that are considered nice to have.

- Twitter developer account (https://developer.twitter.com/en/docs/ads/general/guides/getting-started)
- Kubernetes or Openshift (recommended: minikube 1.5.2)

## Assets

### Postgres
SQL scripts for creating a greetings database.
Shell scripts for starting and initializing the greetings database via docker.

To start the database:

    cd assets/postgres
    ./start.sh
    
### Kafka
Docker compose manifest for starting a zookeepr/kafka cluster on docker.

To start kafka just connect:

    cd assets/kafa
    docker-compose up

## Walkthrough

### Hello world
Let's create a simple Hello World web application!

Create a project from the shell using:

    mvn io.quarkus:quarkus-maven-plugin:1.0.1.Final:create -DprojectGroupId=org.acme -DprojectArtifactId=hello-world -DprojectVersion=0.1-SNAPSHOT -Dendpoint=/hello -DclassName=org.acme.Hello

Open the project and find `Hello.java` to give a glimpse.
From the shell run the application in dev mode:

    mvn compile quarkus:dev

Perform changes to the greeting message and demostrate how dev mode performs changes live, with no rebuilding involved.
From this point on, either align unit tests, or toggle unit tests off `-DskipTests`.

Demonstrate native build.

    mvn clean package -Pnative
    
Run the native application.


    ./target/hello-world-0.1-SNAPSHOT-runner
    
While the application is still running, from another shell check memory consumption:

    ps ax -o pid,rss,command | numfmt --header --from-unit=1024 --to=iec --field 2 | grep -v grep | grep hello
    
At this point the result should be somewhere around `19M` to `21M`.

#### Fun with docker

To emphasize that native binaries need no jvm, change the base image of `Dockerfile.native` to use a minimal glibc based image.
`debian:jessie-slim` is a good example.

Build the image.

     docker build -f src/main/docker/Dockerfile.native -t iocanel/hello-world:0.1-SNAPSHOT .
     
Run the container.

    docker run -it -p 8080:8080 iocanel/hello-world:0.1-SNAPSHOT
    
Show that the application is running as expected.
    
#### Externalize Properties

Optionally, create a `GreetingService` annotated with `javax.enterprise.context.ApplicationScoped` and inject it into `Hello` resource.
Add a `@ConfigProperty(name="message")` annotation onto a String property and crate a method that returns that.
In the `Hello` resource, change the hello method so that it calls this method.
Open `application.properties` and add the value for `message`.

#### Streaming 

From the command line add the reactive stream operators extension.

    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-reactive-streams-operators"
    
    
Create a `StreamingGreetingService` and inject it into `Hello` resource.
Add a String array with greeting messages.
Create a method that returns a `Publisher<String>` and internally uses `Flowable.interval` to return a random greeing message at the specified interval.

```java
String[] greetings = new String[]{"Hola!", "Hi!", "Ciao!", "Bounjour!"};
Random random = new Random();
return Flowable.interval(5, TimeUnit.SECONDS).map(t->greetings[random.nextInt(greetings.length)]);
```

Back to the `Hello` resource. Create a method like `hello` say `stream` that uses a differnt path, say `@Path("/stream")` and insted of `TEXT_PLAIN` return `SERVER_SENT_EVENTS`.
This method should now delegate to the injected `StreamingGreetingService`.


#### Use reactive postgres client.

From the command line add the reactive stream operators extension.

    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-reactive-pg-client"
    
Ensure that postgres is up an running (see [Assets](#Assets)). Describe the greetings table.

Create a `PgGreetingService` that injects a `io.reactiverse.axle.pgclient.PgPool`.
Create a method that runs a simple query like `selet * from greeting where lang='it'`.
From the result get the `greeting` column and return that. The method should return `CompletionStage<String>`.

```java
client.query("select * from greeting where lang='it'").thenApply(rs->rs.iterator().next().getString("greeting"));
```

As before inject the `PgGreetingService` into hello and create a different endpoint.
In the application.properties the following properties are required:

    quarkus.datasource.url=vertx-reactive:postgresql://:5432/demo
    quarkus.datasource.driver=org.postgresql.Driver
    quarkus.datasource.username=root
    quarkus.datasource.password=password 

#### Use hibernate ORM
**NOTE:** Hibernate ORM conflicts with reactive postgres, so start by removing that dependency and code.

Add `hibernate-orm` and `jdbc-postgres` extensions.

    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-hibernate-orm"
    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-jdbc-postgresql"
    
Create a greeting entity with `greeting` and `lang` fields.
Create a `JpaGreetingService` that injects the entity manager and performs a query by id.

    quarkus.datasource.url=jdbc:postgresql:demo
    quarkus.datasource.driver=org.postgresql.Driver
    quarkus.datasource.username=root
    quarkus.datasource.password=password

Finally, inject the `JpaGreetingService` into `Hello` and create a new endpoint to show how it works.
    
#### Use hibernate ORM Panache
Add `hibernate-orm-panace` extensions.

    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-hibernate-orm-panache"
 
Create a new Panache Entity `PanacheGreeting`. The class needs to extend `io.quarkus.hibernate.orm.panache.PanacheEntity` and bear the `javax.persistence.Entity` and `javax.persistence.Table` annotations.

Add two non private fields `greeting` and `lang`.
No constructors, getters or setters are required.

Create a new `PanacheGreetingService` to demonstrate the panache style of ORM.

```java
PanacheGreeting pg = PanacheGreeting.findById(4L);
return pg.greeting;
```

**NOTE:** You cannot use a one liner for the above as `findById` returns a parameterized type and the actual return type needs to be inferred.

Inject the service into hello and create a new endpoint.

#### Use Spring Data

Add `quarkus-spring-web` and `quarkus-spring-jpa` to your project.

Create a `org.springframework.web.bind.annotation.RestController` with `org.springframework.web.bind.annotation.RequestMapping` to `/spring`
Add a method with `org.springframework.web.bind.annotation.GetMapping` `/hello`.

Create new `org.springframework.data.repository.PagingAndSortingRepository` for our `Greeting` entity.

Create a new `org.springframework.stereotype.Service` that has `@Autowired` the greeting repository. This class should provide a method that returns the greeting based on id (see `findById`).

Inject the service into the controller and have the hello method delegate to the service.

    mvn clean quarkus:dev

Open your browser to /spring/hello.

#### Use the rest-client to talk to the application we just created

Create a new application, say `hello-cloud`.

    mvn io.quarkus:quarkus-maven-plugin:1.0.1.Final:create -DprojectGroupId=org.acme -DprojectArtifactId=hello-cloud -DprojectVersion=0.1-SNAPSHOT -Dendpoint=/hello -DclassName=org.acme.Hello

The application is meant to talk with `hello-world` that was created in the previous step.

We are going to need the rest-client and fault tolerance extensions from smallrye.

    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-rest-client"
    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-fault-tolerance"

Create a new `HelloRestClient` that has a hello method with path `/hello` (same as our `hello-world` application).
In the `RegisterRestClient` annotation pass the `baseUri` pointing to the `hello-world` application.

For example:

    @RegisterRestClient(baseUri="http://localhost:8080")
    
Explain how to externalize this to `application.properties`.

    <class name>/mp-rest/url=<actual url>

Create a `HelloRestService` that inject the rest client.
Add a greet method to the service that delegates to the rest client.
Add the `@Fallback` annotation on the method and also add a fallback method.

```java
@Fallback(fallbackMethod="myMethod")
```
Inject and use the service in the `Hello.java` (as we've done many times).

Run the application.

     mvn clean compile quarkus:dev
     
Show normal request.
Shutdown hello-world to demonstrate fallback.

#### Create a twiter to kafka application

**NOTE:** If you don't have a twitter developer account (requires `access tokens` and `consumer keys`), you may skip this step.

Create a new application, say `tweet-writer`.

    mvn io.quarkus:quarkus-maven-plugin:1.0.1.Final:create -DprojectGroupId=org.acme -DprojectArtifactId=tweet-writer -DprojectVersion=0.1-SNAPSHOT
    
Open the project.

Add the `smallrye-reactive-messaging-kafka` extension.

    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-reactive-messaging-kafka"
    
    
Manually add `twitter4j` dependency:

      <dependency>
         <groupId>org.twitter4j</groupId>
         <artifactId>twitter4j-stream</artifactId>
         <version>4.0.6</version>
      </dependency>

Create an application scoped class, that defines a method that returns a `Publisher<String>`.
The method should use `Flowable.create` to caputre the tweets of interest.

    @Outgoing("out-tweet")
    public Publisher<String> stream() {
        return Flowable.create(s -> {
            final TwitterStream twitter = new TwitterStreamFactory().getInstance();
            twitter.addListener(new StatusAdapter() {
                @Override
                public void onStatus(Status status) {
                    System.out.println(status.getText());
                    s.onNext(status.getText());
                }
            });
            twitter.filter(filter);
        }, BackpressureStrategy.DROP);
    }

Where `filter` is an externalized property with the twitter fileter (e.g. `QuarkusIO`).

Open `application.propeties` to define the `out-tweet` sink.
    
    mp.messaging.outgoing.out-tweet.connector=smallrye-kafka
    mp.messaging.outgoing.out-tweet.topic=tweets
    mp.messaging.outgoing.out-tweet.bootstrap.servers=localhost:9092
    mp.messaging.outgoing.out-tweet.key.serializer=org.apache.kafka.common.serialization.StringSerializer
    mp.messaging.outgoing.out-tweet.value.serializer=org.apache.kafka.common.serialization.StringSerializer
    mp.messaging.outgoing.out-tweet.acks=1
    
Create a new resource `twitter4j.properties` and specify the twitter oauth configuration.

    oauth.consumerKey=xxxx
    oauth.consumerSecret=xxxx
    oauth.accessToken=xxxx
    oauth.accessTokenSecret=xxxx
    
Ensure that kafka is running (see [Assets](#Assets)).
Run the application preferably overriding the port (as we are going to start multiple apps).

     mvn clean package
     java -jar target/*-runner.jar -Dquarkus.http.port=8081 -Dtwitter.filter=QuarkusIO

#### Create a kafka to browser application to display tweets via SSE.
Create a new application, say `tweet-reader`.

    mvn io.quarkus:quarkus-maven-plugin:1.0.1.Final:create -DprojectGroupId=org.acme -DprojectArtifactId=tweet-reader -DprojectVersion=0.1-SNAPSHOT -Dendpoint=/hello -DclassName=org.acme.Hello
    
Open the project.

Add the `smallrye-reactive-messaging-kafka` extension.

    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-reactive-messaging-kafka"
    
Open the `Hello` resource and Inject the source stream:

    @Inject
    @Stream("in-tweet")
    Publisher<String> tweets;
    
    
Create a method that produces server sent events and just return the `tweets`.

Configure kafka in the `application.properties`
    
    mp.messaging.incoming.in-tweet.topic=tweets
    mp.messaging.incoming.in-tweet.connector=smallrye-kafka
    mp.messaging.incoming.in-tweet.bootstrap.servers=localhost:9092
    mp.messaging.incoming.in-tweet.key.deserializer=org.apache.kafka.common.serialization.StringDeserializer
    mp.messaging.incoming.in-tweet.value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
    mp.messaging.incoming.in-tweet.group.id=java2days-sofia-2019

**NOTE**: Users the skipped previous step (due to lack of twitter dev account) will be provided access to an external kafka. 

    mp.messaging.incoming.in-tweet.bootstrap.servers=<external kafka socket>

Run the application 

     mvn compile quarkus:dev
     
Open localhost:8080/hello and watch the tweets as they appear.

#### Deploy to Kubernetes
The application can be deployed as is without deploying and configuring a database on Kubernetes.
The simple `/hello` and `/hello/stream` endpoint will still work without issues (The ones that do require a database, of course won't work)

Add the `kubernetes` extension.

    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-kubernetes"
    
Build the application.

    mvn clean install

Perform docker build and push (to dockerhub).

     docker build -f src/main/docker/Dockerfile.native -t iocanel/hello-world:0.1-SNAPSHOT .
     docker push iocanel/hello-world:0.1-SNAPSHOT

Open the generated manifest `target/wiring-classes/META-INF/kubernetes/kubernetes.yml`.

Apply the generated resources.

    kubectl create -f target/wiring-classes/META-INF/kubernetes/kubernetes.yml
    
Expose the application to the real world, either by `Ingress` or `Route` (if used on Openshift).

Show the `/hello` is working as expected.


#### Remote dev mode on Kubernetes

Open the hello-world application.

Add the `undertow-websockets` extension.

    mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-undertow-websockets"
    
Open the `application.propeties` so that we can specify the live reload password.

    quarkus.live-reload.password=secret
    
    
Create `Dockerfile.remote` so that we have a container capable of building quarkus that includes project sources:

    FROM fabric8/java-centos-openjdk8-jdk
    ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
    ENV AB_ENABLED=jmx_exporter
    COPY mvnw /deployments/mvnw
    COPY .mvn /deployments/.mvn
    COPY src /deployments/src
    COPY pom.xml /deployments/pom.xml

    WORKDIR /deployments
    USER root
    RUN chown -R jboss src
    USER jboss
    ENTRYPOINT [ "./mvnw", "compile", "quarkus:dev" ]
    
Build the Dockerfile:

     docker build -f src/main/docker/Dockerfile.remote -t iocanel/hello-world:0.1-SNAPSHOT .

Push the image:

     docker push iocanel/hello-world:0.1-SNAPSHOT

Edit the deploynent and change image pull policy from `IfNotPresent` to `Always`.

Once the application running on kubernets is restarted. 
From the project run `remote-dev` and pass the url of the application.

    mvn quarkus:remote-dev -Dquarkus.live-reload.url=http://hello-world-iocanel.195.201.87.126.nip.io
    
Perform changes to the message and demonstrate them being applied live!
 
## Milestones
- [Hello World](https://github.com/iocanel/java2days-sofia-2019/tree/01-hello-world)
- [Hello World with Externalized Property](https://github.com/iocanel/java2days-sofia-2019/tree/02-hello-world-with-externalized-property)
- [Hello World with Reactive](https://github.com/iocanel/java2days-sofia-2019/tree/03-hello-world-with-reactive)
- [Hello World with Reactive Postgres](https://github.com/iocanel/java2days-sofia-2019/tree/04-hello-world-with-reactive-postgres)
- [Hello World with ORM](https://github.com/iocanel/java2days-sofia-2019/tree/05-hello-world-with-orm)
- [Hello World with Panache](https://github.com/iocanel/java2days-sofia-2019/tree/06-hello-world-with-panache)
- [Hello Cloud with Rest Client and Fault Tolerance](https://github.com/iocanel/java2days-sofia-2019/tree/08-hello-cloud-with-rest-client-and-fault-tolernace)
- [Tweet Writer (Kafka)](https://github.com/iocanel/java2days-sofia-2019/tree/09-tweet-kafka-writer)
- [Tweet Reader (Kafka)](https://github.com/iocanel/java2days-sofia-2019/tree/10-tweet-kafka-reader)
- [Hello World on Kubernetes](https://github.com/iocanel/java2days-sofia-2019/tree/07-hello-world-on-kubernetes)
- [Remote Dev Mode](https://github.com/iocanel/java2days-sofia-2019/tree/11-remote-dev)
