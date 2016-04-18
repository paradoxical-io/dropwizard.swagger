package io.paradoxical.dropwizard.swagger;

import io.dropwizard.jetty.MutableServletContextHandler;
import io.dropwizard.setup.Environment;
import lombok.AccessLevel;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.function.Function;

public abstract class EnvironmentSwaggerConfiguration extends SwaggerConfiguration {

    @Getter(AccessLevel.PROTECTED)
    private final Environment environment;

    protected EnvironmentSwaggerConfiguration(
        @Nullable final Environment environment,
        final Function<Environment, MutableServletContextHandler> contextSelector) {

        this.environment = environment;

        if (environment != null) {
            setBasePath(contextSelector.apply(environment).getContextPath());
        }
    }
}
