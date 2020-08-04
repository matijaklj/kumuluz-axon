# KumuluzEE Axon Extension

KumuluzEE Axon Extension project allows an easy way to integrate the Axon framework for developing CQRS and DDD applications with the KumuluzEE microservice framework.
The extension allows simple straightforward configuration of Axon components and offers annotation for auto-configuring aggregates and aggregates repositories. 
Furthermore the extension automatically registers all annotated event, command and query handlers.

## Usage

You can enable KumuluzEE Axon by adding the following dependency:

```xml
<dependency>
    <groupId>com.kumuluz.ee.axon</groupId>
    <artifactId>kumuluzee-axon</artifactId>
    <version>${kumuluzee-axon.version}</version>
</dependency>
```

### Automatic Configuration
The base Axon Framework is extremely powerful and flexible. What this extension does is to provide a number of sensible defaults for Axon applications while still allowing you reasonable configuration flexibility - including the ability to override defaults. As soon as you include the module in your project, you will be able to inject a number of Axon APIs into your code using CDI. These APIs represent the most important Axon Framework building blocks:

* [CommandBus](http://www.axonframework.org/apidocs/4.3/org/axonframework/commandhandling/CommandBus.html)
* [CommandGateway](http://www.axonframework.org/apidocs/4.3/org/axonframework/commandhandling/gateway/CommandGateway.html)
* [EventBus](http://www.axonframework.org/apidocs/4.3/org/axonframework/eventhandling/EventBus.html)
* [EventGateway](https://apidocs.axoniq.io/4.3/org/axonframework/eventhandling/gateway/EventGateway.html)
* [QueryBus](http://www.axonframework.org/apidocs/4.3/org/axonframework/queryhandling/QueryBus.html)
* [QueryGateway](http://www.axonframework.org/apidocs/4.3/org/axonframework/queryhandling/QueryGateway.html)
* [Serializer](http://www.axonframework.org/apidocs/4.3/org/axonframework/serialization/Serializer.html)
* [Configuration](http://www.axonframework.org/apidocs/4.3/org/axonframework/config/Configuration.html)

### Override configurations
You can provide configuration overrides for the following Axon artifacts by creating CDI producers for them:

* [EntityManagerProvider](http://www.axonframework.org/apidocs/4.3/org/axonframework/common/jpa/EntityManagerProvider.html)
* [EventStorageEngine](http://www.axonframework.org/apidocs/4.3/org/axonframework/eventsourcing/eventstore/EventStorageEngine.html)
* [TransactionManager](http://www.axonframework.org/apidocs/4.3/org/axonframework/common/transaction/TransactionManager.html) (in case of JTA, make sure this is a transaction manager that will work with JTA. For your convenience, we have provided a JtaTransactionManager that should work in most CMT and BMT situations.)
* [EventBus](http://www.axonframework.org/apidocs/4.3/org/axonframework/eventhandling/EventBus.html)
* [EventGateway](https://apidocs.axoniq.io/4.3/org/axonframework/eventhandling/gateway/EventGateway.html)
* [CommandBus](http://www.axonframework.org/apidocs/4.3/org/axonframework/commandhandling/CommandBus.html)
* [QueryBus](http://www.axonframework.org/apidocs/4.3/org/axonframework/queryhandling/QueryBus.html)
* [CommandGateway](http://www.axonframework.org/apidocs/4.3/org/axonframework/commandhandling/gateway/CommandGateway.html)
* [QueryGateway](http://www.axonframework.org/apidocs/4.3/org/axonframework/queryhandling/QueryGateway.html)
* [TokenStore](http://www.axonframework.org/apidocs/4.3/org/axonframework/eventhandling/tokenstore/TokenStore.html)
* [Serializer](http://www.axonframework.org/apidocs/4.3/org/axonframework/serialization/Serializer.html) (both a global serializer and an event serializer may be overriden. To override an event serializer, please name the producer "eventSerializer" via the @Named annotation. It is purely optional, but you can use @Named to name your global serializer "serializer". If no @Named annotation is present, the serializer is assumed to be global)
* [ErrorHandler](http://www.axonframework.org/apidocs/4.3/org/axonframework/eventhandling/ErrorHandler.html)
* [ListenerInvocationErrorHandler](https://github.com/AxonFramework/AxonFramework/blob/master/core/src/main/java/org/axonframework/eventhandling/ListenerInvocationErrorHandler.java)
* [CorrelationDataProvider](https://github.com/AxonFramework/AxonFramework/blob/master/core/src/main/java/org/axonframework/messaging/correlation/CorrelationDataProvider.java)
* [EventUpcaster](http://www.axonframework.org/apidocs/4.3/org/axonframework/serialization/upcasting/event/EventUpcaster.html)
* [Configurer](http://www.axonframework.org/apidocs/4.3/org/axonframework/config/Configurer.html)
* TODO [ModuleConfiguration](http://www.axonframework.org/apidocs/4.3/org/axonframework/config/ModuleConfiguration.html))

## Running Axon Server

By default the Axon Framework is configured to expect a running Axon Server instance, and it will complain if the server is not found. To run Axon Server, you'll need a Java runtime (JRE versions 8 through 10 are currently supported). You can run it locally, in a Docker container (including Kubernetes or even Mini-kube), or on a separate server.


To run Axon Server in Docker you can use the image provided on Docker Hub:

```
$ docker run -d --name my-axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver
...some container id...
$
```

*WARNING* This is not a supported image for production purposes. Please use with caution.

If you want to run the clients in Docker containers as well, and are not using something like Kubernetes, use the "`--hostname`" option of the `docker` command to set a useful name like "axonserver", and pass the `AXONSERVER_HOSTNAME` environment variable to adjust the properties accordingly:

```
$ docker run -d --name my-axon-server -p 8024:8024 -p 8124:8124 --hostname axonserver -e AXONSERVER_HOSTNAME=axonserver axoniq/axonserver
```

When you start the client containers, you can now use "`--link axonserver`" to provide them with the correct DNS entry. The Axon Server-connector looks at the "`axon.axonserver.servers`" property to determine where Axon Server lives, so don't forget to set it to "`axonserver`".

### Disabling extension

The extension can be disabled by setting the `kumuluzee.axon.enabled` configuration property to `false`.

## Changelog

Recent changes can be viewed on Github on the [Releases Page](https://github.com/kumuluz/kumuluzee-axon/releases)

## Contribute

See the [contributing docs](https://github.com/kumuluz/kumuluzee-axon/blob/master/CONTRIBUTING.md)

When submitting an issue, please follow the [guidelines](https://github.com/kumuluz/kumuluzee-axon/blob/master/CONTRIBUTING.md#bugs).

Issues related to KumuluzEE itself should be submitted at https://github.com/kumuluz/kumuluzee/issues.

## License

MIT
