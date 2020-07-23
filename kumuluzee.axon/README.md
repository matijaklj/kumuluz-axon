# KumuluzEE Axon extension
Auto configure Axon framework with KumuluzEE

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

