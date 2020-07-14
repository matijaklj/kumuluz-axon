package com.kumuluz.ee.samples.kumuluzee.axon;

import com.kumuluz.ee.samples.kumuluzee.axon.stereotype.Aggregate;
import com.kumuluz.ee.samples.kumuluzee.axon.stereotype.AggregateRepository;
import org.axonframework.config.AggregateConfigurer;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.modelling.command.Repository;

import javax.enterprise.inject.spi.BeanManager;
import java.util.List;
import java.util.Optional;

public class AxonConfigurationInitializer {


    static Configurer initializeAxonConfigurer() {

        // todo check kumuluz config for axon config init setting
        // if initialize default or jpa configs
        Configurer configurer = DefaultConfigurer.defaultConfiguration();

        // todo check kumuluz config for auto-detect-aggregates or manually register them...

        return  configurer;
    }


    static Configurer registerAnnotatedAggregates(BeanManager bm,
                                                         Configurer configurer,
                                                         List<AnnotatedInstance<Aggregate>> instanceList,
                                                         List<AnnotatedInstance<AggregateRepository>> repoInstanceList) {
        for(AnnotatedInstance<Aggregate> instance : instanceList) {
            Class beanClass = instance.getBean().getBeanClass();

            String repoName = instance.getAnnotation().repository();
            if (repoName.isEmpty()) {
                repoName = beanClass.getName();
                repoName = Character.toLowerCase(repoName.charAt(0)) + repoName.substring(1) + "Repository";
            }

            final String finalRepoName = repoName;
            Optional<AnnotatedInstance<AggregateRepository>> repoInstance = repoInstanceList.stream().filter(i -> i.getMethod().getName().equals(finalRepoName)).findFirst();

            if (!repoInstance.isPresent()) {
                // no annotated repository present, aggregate is registered as an event sourced agg.
                configurer.configureAggregate(beanClass);
            } else {
                Repository repo = null;
                Object inst = bm.getReference(repoInstance.get().getBean(), repoInstance.get().getMethod().getDeclaringClass(), bm
                        .createCreationalContext(repoInstance.get().getBean()));

                // todo kdaj se to izvede, pred ali po izvedbi konfiguracije in configa eventbusa itd... mogoče bi se tukaj moralo injectat kumuluz axon config = AxonConfig
                try{
                    repo = (Repository) repoInstance.get().getMethod().invoke(inst, configurer.buildConfiguration());
                } catch (Exception e) {
                    //log.warning(e.getMessage());
                }
                final Repository aggregateRepository = repo;
                configurer.configureAggregate(

                    AggregateConfigurer.defaultConfiguration(beanClass.getClass())
                            .configureRepository(c -> aggregateRepository)
                );
            }

            configurer.configureAggregate(beanClass);
            /*configurer.configureAggregate(

                    AggregateConfigurer.defaultConfiguration(beanClass.getClass())
                        .configureRepository(c -> GenericJpaRepository.builder(GiftCard.class).entityManagerProvider(e).eventBus(c.eventBus()).build())
            );*/
        }

        return configurer;
    }
}
