package com.kumuluz.ee.kumuluzee.axon.util;

import javax.enterprise.inject.spi.AnnotatedType;
import java.lang.annotation.Annotation;

public class CDIUtilities {

    public static boolean hasAnnotation(AnnotatedType<?> at, Class<? extends Annotation> annotationClass) {
        return at.getMethods().stream().anyMatch(m -> m.isAnnotationPresent(annotationClass));
    }

}
