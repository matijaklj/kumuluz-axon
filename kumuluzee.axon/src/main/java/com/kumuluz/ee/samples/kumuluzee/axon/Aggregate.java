package com.kumuluz.ee.samples.kumuluzee.axon;


import org.axonframework.modelling.command.AggregateRoot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AggregateRoot
public @interface Aggregate {
    String repository() default "";

    String snapshotTriggerDefinition() default "";

    String type() default "";

    String commandTargetResolver() default "";

    boolean filterEventsByType() default false;
}
