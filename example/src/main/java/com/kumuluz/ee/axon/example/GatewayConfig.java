package com.kumuluz.ee.axon.example;

import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;

import javax.enterprise.context.ApplicationScoped;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@ApplicationScoped
public class GatewayConfig {

    private static final Logger log = Logger.getLogger(
            MethodHandles.lookup().lookupClass().getName());


    /*
    @Produces
    @ApplicationScoped
    @Named("serializer")
    public Serializer getGeneralSerializer() {
        return XStreamSerializer.builder().build();
    }

    @Produces
    @ApplicationScoped
    @Named("eventSerializer")
    public Serializer getEventSerializer() {
        return XStreamSerializer.builder().build();
    }

    @Produces
    @ApplicationScoped
    public EntityManager entityManager() {
        try {
            EntityManager em = Persistence.createEntityManagerFactory("giftcard")
                    .createEntityManager();
            //return (EntityManager) new InitialContext().lookup("java:jdbc/GiftCardDS");
            return em;
        } catch (Exception ex) {
            log.warning("Failed to look up entity manager. Error: " + ex.toString());
        }

        return null;
    }

    @Produces
    @ApplicationScoped
    public TransactionManager transactionManager() {
        //return new JtaTransactionManager();
        return NoTransactionManager.INSTANCE;
    }

    @Produces
    @ApplicationScoped
    public CommandBus myCommandBus(Configuration configuration, TransactionManager transactionManager) {
        return SimpleCommandBus.builder()
                .rollbackConfiguration(RollbackConfigurationType.NEVER)
                .transactionManager(transactionManager)
                .messageMonitor(configuration.messageMonitor(CommandBus.class, "commandBus"))
                .build();
    }



    @Produces
    @ApplicationScoped
    public CommandGateway myCommandGateway(Configuration configuration) {

        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        CommandGatewayFactory factory = CommandGatewayFactory.builder()
                .commandBus(configuration.commandBus())
                .retryScheduler(
                        IntervalRetryScheduler.builder().retryExecutor(scheduler).retryInterval(11).build())
                .build();

        return factory.createGateway(MyCommandGateway.class);
    }

    @Produces
    @ApplicationScoped
    public CorrelationDataProvider messageOriginProvider() {
        return new MessageOriginProvider();
    }

    @Produces
    @ApplicationScoped
    public CorrelationDataProvider SimpleCorrelationDataProvider() {
        return new SimpleCorrelationDataProvider();
    }

     */
    /*
    @Produces
    @ApplicationScoped
    public EntityManagerProvider entityManagerProvider(
            EntityManager entityManager) {
        return new SimpleEntityManagerProvider(entityManager);
    }


    @Produces
    @ApplicationScoped
    public Repository<GiftCard> myGiftCardRepository(Configuration configuration,
                                                     EntityManagerProvider entityManagerProvider) {
        return GenericJpaRepository
                .builder(GiftCard.class)
                .entityManagerProvider(entityManagerProvider)
                .eventBus(configuration.eventBus())
                .build();
    }

     */


}

