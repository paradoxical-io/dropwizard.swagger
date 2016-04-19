package io.paradoxical.dropwizard.swagger;

import lombok.Getter;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

public class SwaggerFilters {

    @Getter
    private final Predicate<Class<?>> filterPredicate;

    protected SwaggerFilters(final Predicate<Class<?>> filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    public boolean shouldInclude(Class<?> type) {
        return filterPredicate.test(type);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static SwaggerFilters withAnnotation(@NonNull Class<? extends Annotation> annotation) {
        return builder().withAnnotation(annotation)
                        .build();
    }

    public static SwaggerFilters withoutAnnotation(@NonNull Class<? extends Annotation> annotation) {
        return builder().withoutAnnotation(annotation)
                        .build();
    }

    public static class Builder {

        private Predicate<Class<?>> filterPredicate = cls -> true;

        public Builder withAnnotation(@NonNull Class<? extends Annotation> annotation) {
            filterPredicate = filterPredicate.and(type -> type.isAnnotationPresent(annotation));
            return this;
        }

        public Builder withoutAnnotation(@NonNull Class<? extends Annotation> annotation) {
            filterPredicate = filterPredicate.and(type -> !type.isAnnotationPresent(annotation));
            return this;
        }

        public SwaggerFilters build() {
            return new SwaggerFilters(filterPredicate);
        }

    }
}
