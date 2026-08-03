package ru.basted.basteduitests.annotations;

import java.lang.annotation.Annotation;

import org.jspecify.annotations.Nullable;

public final class AnnotationUtils {
    private AnnotationUtils() {
        throw new UnsupportedOperationException("Запрещено создание экземпляра утилитарного класса");
    }

    public static <A extends Annotation> @Nullable A getAnnotation(Class<?> pageClass, Class<A> annotationClass) {
        return pageClass.getAnnotation(annotationClass);
    }
}
