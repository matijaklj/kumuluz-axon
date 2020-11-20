# KumuluzEE Axon sample Gift Card application

> Develop a sample Gift Catd application based on CQRS using the Axon framework and pack it as a KumuluzEE microservice.

The objective of this sample is to demonstrate how to develop a simple Gift Card application based on the CQRS 
architectural pattern to showcase the functionality of the Axon and KumuuzEE framework. The tutorial guides you through the development and 
coagulation of basic Axon components to build a simple Gift Card application and shows how to pack it as a KumuluzEE 
microservice. To develop our Gift Card application, we firstly create the command, query and event classes. Then we 
develop the GiftCard aggregate and command handlers on the command side and the event handlers and query handlers on the 
query side. Finally we create a REST service to send commands and query data in our application. 


## Requirements

In order to run this example you will need the following:

1. Java 8 (or newer), you can use any implementation:
    * If you have installed Java, you can check the version by typing the following in a command line:
        
        ```
        java -version
        ```

2. Maven 3.2.1 (or newer):
    * If you have installed Maven, you can check the version by typing the following in a command line:
        
        ```
        mvn -version
        ```
3. Git:
    * If you have installed Git, you can check the version by typing the following in a command line:
    
        ```
        git --version
        ```
    
## Prerequisites

In order to run this sample you will have to setup a local Axon Server on the port 8024.

You can run the Axon Server inside Docker:
```
docker run -d --name my-axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver
```

## Usage

The example uses maven to build and run the microservices.

1. Build the sample using maven:

    ```bash
    $ cd sample
    $ mvn clean package
    ```

2. Run the sample:
* Uber-jar:

    ```bash
    $ java -jar target/${project.build.finalName}.jar
    ```
    
    in Windows environemnt use the command
    ```batch
    java -jar target/${project.build.finalName}.jar
    ```

* Exploded:

    ```bash
    $ java -cp target/classes:target/dependency/* com.kumuluz.ee.EeApplication
    ```
    
    in Windows environment use the command
    ```batch
    java -cp target/classes;target/dependency/* com.kumuluz.ee.EeApplication
    ```
    
    
The application/service can be accessed on the following URLs:
* JAX-RS REST commands resource page - http://localhost:8080/v1/commands 
* JAX-RS REST queries resource page - http://localhost:8080/v1/queries 

To shut down the example simply stop the processes in the foreground.

## Tutorial

This tutorial will guide you through the steps required to create a simple Gift Card application based on the CQRS
pattern. Firstly we implement the Axon messages classes for commands, events and queries. Then we implement the command
side aggregate class with command handlers. For the query side we implement the event and query handlers and the query
side projection storage. Lastly we implement a simple REST service for sending commands and queries to our application.

We will follow these steps:
* Complete the tutorial for [KumuluzEE JAX-RS REST sample](https://github.com/kumuluz/kumuluzee-samples/tree/master/jax-rs) or clone the existing sample
* Ensure access to the Axon Server.
* Add Maven dependencies
* Implement the query, command and event classes
* Implement the command side aggregate class and command handlers
* Implement the query side event and query handlers
* Implement REST endpoints to send commands and query data
* Build the microservice
* Run it

### Add Maven dependencies

Add the KumuluzEE BOM module dependency to your `pom.xml` file:
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.kumuluz.ee</groupId>
            <artifactId>kumuluzee-bom</artifactId>
            <version>${kumuluz.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Add the `kumuluzee-microProfile-1.0`, which adds the MicroProfile 1.0 dependencies (JAX-RS, CDI, JSON-P, and Servlet).

```xml
<dependency>
    <groupId>com.kumuluz.ee</groupId>
    <artifactId>kumuluzee-microProfile-1.0</artifactId>
    <version>3.6.0</version>
</dependency>
```

Add the `kumuluz-axon` extension and `axon-server-connector` dependencies.
```xml
<dependency>
    <groupId>com.kumuluz.ee.axon</groupId>
    <artifactId>kumuluzee-axon</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
                
<dependency>
    <groupId>org.axonframework</groupId>
    <artifactId>axon-server-connector</artifactId>
    <version>${axon.version}</version>
</dependency>
```

And the `mapdb` dependency to implement the concurrent map for the query side storage.
```xml
<dependency>
    <groupId>org.mapdb</groupId>
    <artifactId>mapdb</artifactId>
    <version>3.0.7</version>
</dependency>
```

Add the `kumuluzee-maven-plugin` build plugin to package microservice as uber-jar:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.kumuluz.ee</groupId>
            <artifactId>kumuluzee-maven-plugin</artifactId>
            <version>${kumuluzee.version}</version>
            <executions>
                <execution>
                    <id>package</id>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

or exploded:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.kumuluz.ee</groupId>
            <artifactId>kumuluzee-maven-plugin</artifactId>
            <version>${kumuluzee.version}</version>
            <executions>
                <execution>
                    <id>package</id>
                    <goals>
                        <goal>copy-dependencies</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Implement the command, event and query classes
Firstly you have to implement the java classes for commands, events and queries in our Gift Card application.

Commands in the Axon framework are POJOs with a field annotated with `TargetAggregateIdentifier` annotation, which 
is the reference to the aggregate that handles the command.
Below is an example of implementation of `IssueCommand`, similarly we implement the `RedeemCommand`.
```java
public class IssueCommand {

    @TargetAggregateIdentifier
    private String id;
    private Integer amount;
    
    // constructor, getters and setters...
}
```

After the command will be applied to the aggregate an event will be published. We have to implement two events:
`IssuedEvent` and `RedeemedEvent`.

```java
public class IssuedEvent {

    private String id;
    private Integer amount;
    
    // constructor, getters and setters...
}
```

To query data in our application we will implement an query message class `FindGiftCardQuery`.

```java
public class FindGiftCardQuery{
    private String id;
    
    // constructor, getters and setters...
}
```

For storing the query projections on the query side we need to create a `GiftCardRecord` DTO. 

```java
public class GiftCardRecord implements Serializable {

    private String id;
    private Integer initialValue;
    private Integer remainingValue;

    // constructor, getters and setters...
}
```

### Implement the command side
On the command side we have to implement our event sourced aggregate class `GiftCard`.
Below is the implementation code for our aggregate, its important that we annotate the class with `@Aggregate` and
`@ApplicationScoped` annotation so that the aggregate is automatically registered with the Axon configuration.
Next we have to annotate the field that represents the external reference point of the aggregate with the `@AggregateIdentifier` 
annotation. Finally we implement the command handlers methods annotated with `@CommandHandler` annotations and 
the event sourcing event handlers annotated with `@EventSourcingHandler` annotation.
The Axon framework requires a no-arg constructor for the aggregate class, which is used for creating an empty aggregate 
instance before initializing it using past Events. 

```java
@Aggregate
@ApplicationScoped
public class GiftCard {

    @AggregateIdentifier
    private String id;
    private int remainingValue;

    @CommandHandler
    public GiftCard(IssueCommand cmd) {
        log.info("handling {}", cmd);
        if (cmd.getAmount() <= 0) throw new IllegalArgumentException("amount <= 0");
        apply(new IssuedEvent(cmd.getId(), cmd.getAmount()));
    }

    @EventSourcingHandler
    void createGiftCard(IssuedEvent evt) {
        log.info("applying {}", evt);
        id = evt.getId();
        remainingValue = evt.getAmount();
        log.debug("new remaining value: {}", remainingValue);
    }

    // Required by Axon
    public GiftCard() {
    }
    
    // omitted other command handlers and event sourcing handlers
}
```

### Implement the query side
For simplicity we will use a concurrent map for storing query side projections of our Gift Card data.
We will use `mapdb` to create an in-memory database for our query projections.
```java
public class GiftCardQueryConfiguration {
    @Produces
    @ApplicationScoped
    public ConcurrentMap<String, GiftCardRecord> querySideMap() {
        DB querySideDB = DBMaker.memoryDB().make();
        return (ConcurrentMap<String, GiftCardRecord>) querySideDB.hashMap("querySideDBMap").createOrOpen();
    }
}
```

Next we implement the query side event handlers, that subsribe to published event in our app and update the query
side db.

```java
@ApplicationScoped
public class GiftCardEventHandler {

    @Inject
    Configuration configuration;

    @Inject
    private ConcurrentMap<String, GiftCardRecord> querySideMap;

    public GiftCardEventHandler() {

    }

    @EventHandler
    void on(IssuedEvent event) {
        log.info("query side handling {}", event);

        GiftCardRecord record = new GiftCardRecord(event.getId(), event.getAmount(), event.getAmount());
        querySideMap.put(event.getId(), record);

        this.configuration.queryUpdateEmitter().emit(FindGiftCardQuery.class, findGiftCardQuery -> Objects.equals(event.getId(), findGiftCardQuery.getId()), record);
    }

    // omitted event handlers for other events
}
```

Finally we implement the query handlers annotated with the `@QueryHandler`.

```java
@ApplicationScoped
public class GiftCardQueryHandler {

    @Inject
    private ConcurrentMap<String, GiftCardRecord> querySideMap;

    public GiftCardQueryHandler() {}

    @QueryHandler
    public GiftCardRecord handle(FindGiftCardQuery query) {
        log.info("handling {}", query);

        return querySideMap.getOrDefault(query.getId(), new GiftCardRecord("0", Integer.MIN_VALUE, Integer.MIN_VALUE));
    }
}
```

### Implement REST endpoints to send commands and query data

To send commands to our app we implement a REST endpoint `commands` that publishes commands to the Axon Server.
We need to inject the `CommandGateway` that we use to send commands.

```java
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("commands")
public class CommandApi {

    @Inject
    private CommandGateway commandGateway;

    @POST
    public Response issueGiftCard(IssueCommand issueCommand) {

        CompletableFuture futureResult = this.commandGateway.send(issueCommand);
        try {
            return Response.ok(futureResult.get()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
```

Similarly we implement a REST endpoint `queries` that sends queries to the Axon Server and returns Gift Card projections.
To send queries we need to first inject the `QueryGateway` into our code. 

```java
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("queries")
public class QueryApi {

    @Inject
    private QueryGateway queryGateway;

    @GET
    @Path("{id}")
    public Response getGiftCardById(@PathParam("id") String id) throws ExecutionException, InterruptedException {

        FindGiftCardQuery q = new FindGiftCardQuery(id);

        GenericQueryMessage<FindGiftCardQuery, GiftCardRecord> query =
                new GenericQueryMessage<>(q, ResponseTypes.instanceOf(GiftCardRecord.class));
        GiftCardRecord result = queryGateway.query(q, GiftCardRecord.class).get();

        return Response.ok(result).build();
    }
}
```

### Build the microservice and run it

To build the microservice and run the example, use the commands as described in previous sections.