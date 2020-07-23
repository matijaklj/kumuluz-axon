package com.kumuluz.ee.kumuluzee.axon;

import com.kumuluz.ee.kumuluzee.axon.stereotype.Aggregate;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

/**
 * AggregateDefinition class for detected annotated aggregate classes.
 * This was adapted from Axon extension-cdi:
 * https://github.com/AxonFramework/extension-cdi
 * @author Milan Savic
 */
class AggregateDefinition {

    private final Class<?> aggregateType;

    AggregateDefinition(Class<?> aggregateType) {
        this.aggregateType = aggregateType;
    }

    Class<?> aggregateType() {
        return aggregateType;
    }

    Optional<String> repository() {
        return StringUtilities.createOptional(getAggregateAnnotation().repository());
    }

    String repositoryName() {
        return repository().orElse(StringUtilities.lowerCaseFirstLetter(
                aggregateType().getSimpleName()) + "Repository");
    }

    /*Optional<String> snapshotTriggerDefinition() {
        return StringUtilities.createOptional(getAggregateAnnotation()
                .snapshotTriggerDefinition());
    }

    Optional<String> type() {
        return StringUtilities.createOptional(getAggregateAnnotation().type());
    }

    Optional<String> commandTargetResolver() {
        return StringUtilities.createOptional(getAggregateAnnotation()
                .commandTargetResolver());
    }
     */

    private Aggregate getAggregateAnnotation() {
        return aggregateType.getAnnotation(Aggregate.class);
    }

    boolean isJpaAggregate() {
        return Arrays.stream(aggregateType.getAnnotations())
                .map(Annotation::annotationType)
                .map(Class::getName)
                .anyMatch("javax.persistence.Entity"::equals);
    }
}
