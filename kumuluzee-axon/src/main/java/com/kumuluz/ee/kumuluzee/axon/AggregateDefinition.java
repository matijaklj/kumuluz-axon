/*
 *  Copyright (c) 2014-2017 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.kumuluz.ee.kumuluzee.axon;

import com.kumuluz.ee.kumuluzee.axon.stereotype.Aggregate;
import com.kumuluz.ee.kumuluzee.axon.util.StringUtilities;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

/**
 * AggregateDefinition class for detected annotated aggregate classes.
 * This was adapted from Axon extension-cdi:
 * https://github.com/AxonFramework/extension-cdi
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
