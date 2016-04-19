package io.paradoxical.dropwizard.swagger;

import io.swagger.jaxrs.config.BeanConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;


public class SwaggerConfiguration extends BeanConfig {

    @Getter
    @Setter
    private SwaggerFilters filters;

    @Override
    public Set<Class<?>> classes() {
        return super.classes().stream()
                    .filter(filters::shouldInclude)
                    .collect(Collectors.toSet());
    }
}

