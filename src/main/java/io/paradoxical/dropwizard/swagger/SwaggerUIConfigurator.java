package io.paradoxical.dropwizard.swagger;

import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.mustache.MustacheViewRenderer;
import io.paradoxical.dropwizard.bundles.admin.AdminEnvironmentConfigurator;
import io.paradoxical.dropwizard.bundles.admin.AdminResourceEnvironment;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import lombok.NonNull;

import java.util.function.Function;

public class SwaggerUIConfigurator implements AdminEnvironmentConfigurator {
    private final SwaggerResourcesLocator.Factory swaggerResourcesLocatorFactory;

    @SuppressWarnings("WeakerAccess")
    public SwaggerUIConfigurator(@NonNull final SwaggerResourcesLocator.Factory swaggerResourcesLocatorFactory) {
        this.swaggerResourcesLocatorFactory = swaggerResourcesLocatorFactory;
    }

    @SuppressWarnings("WeakerAccess")
    public SwaggerUIConfigurator(@NonNull final SwaggerResourcesLocator swaggerResourcesLocator) {
        this(env -> swaggerResourcesLocator);
    }

    @SuppressWarnings("unused")
    public static SwaggerUIConfigurator forConfig(@NonNull final SwaggerConfiguration swaggerConfiguration) {
        return new SwaggerUIConfigurator(new DefaultSwaggerResourcesLocator(swaggerConfiguration));
    }

    @SuppressWarnings("unused")
    public static SwaggerUIConfigurator forConfig(@NonNull final Function<Environment, SwaggerConfiguration> envConfigFunction) {
        return new SwaggerUIConfigurator(env -> new DefaultSwaggerResourcesLocator(envConfigFunction.apply(env)));
    }

    public void configure(Environment environment, JerseyEnvironment jerseyEnvironment) {

        final ViewMessageBodyWriter viewMessageBodyWriter =
            new ViewMessageBodyWriter(environment.metrics(),
                                      ImmutableList.of(new MustacheViewRenderer()));

        jerseyEnvironment.register(viewMessageBodyWriter);
        jerseyEnvironment.register(new SwaggerSerializers());
        jerseyEnvironment.register(swaggerResourcesLocatorFactory.forEnvironment(environment));
    }

    @Override
    public void configure(Configuration configuration, AdminResourceEnvironment adminResourceEnvironment) {
        AdminEnvironmentConfigurator.forJersey(this::configure).configure(configuration, adminResourceEnvironment);
    }
}
