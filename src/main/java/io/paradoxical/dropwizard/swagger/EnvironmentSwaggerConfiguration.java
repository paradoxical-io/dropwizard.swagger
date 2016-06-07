package io.paradoxical.dropwizard.swagger;

import io.dropwizard.jetty.MutableServletContextHandler;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import lombok.AccessLevel;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.function.Function;

public abstract class EnvironmentSwaggerConfiguration extends SwaggerConfiguration {

    @Getter(AccessLevel.PROTECTED)
    private final Environment environment;

    /**
     * Creates an environment specific swagger configuration,
     * optionally defaulting the swagger base path.
     * See the {@link BeanConfig#setBasePath(String)} method for details.
     * @param environment the environment
     * @param contextSelector the environment servlet context selector to get the base path from
     *                        using the {@link MutableServletContextHandler#getContextPath()} method.
     *                        @apiNote usually this will be one of either
     *                          {@link Environment#getAdminContext() Environment::getAdminContext}
     *                          or {@link Environment#getApplicationContext() Environment::getApplicationContext}
     *
     */
    protected EnvironmentSwaggerConfiguration(
        @Nullable final Environment environment,
        @Nullable final Function<Environment, MutableServletContextHandler> contextSelector) {

        this.environment = environment;

        if (environment != null) {

            if(contextSelector == null) {
                throw new IllegalArgumentException("contextSelector is null but the environment is null null");
            }

            setBasePath(contextSelector.apply(environment).getContextPath());
        }
    }
}
