package io.paradoxical.dropwizard.swagger;

import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.mustache.MustacheViewRenderer;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import lombok.NonNull;

import java.util.Collections;
import java.util.function.Function;

public class SwaggerUIConfigurator {
    private final SwaggerResourcesLocator.Factory swaggerResourcesLocatorFactory;

    public SwaggerUIConfigurator(@NonNull final SwaggerResourcesLocator.Factory swaggerResourcesLocatorFactory) {
        this.swaggerResourcesLocatorFactory = swaggerResourcesLocatorFactory;
    }

    public SwaggerUIConfigurator(@NonNull final SwaggerResourcesLocator swaggerResourcesLocator) {
        this(env -> swaggerResourcesLocator);
    }

    public SwaggerUIConfigurator forConfig(@NonNull final SwaggerConfiguration swaggerConfiguration) {
        return new SwaggerUIConfigurator(new DefaultSwaggerResourcesLocator(swaggerConfiguration));
    }

    public SwaggerUIConfigurator forConfig(@NonNull final Function<Environment, SwaggerConfiguration> envConfigFunction) {
        return new SwaggerUIConfigurator(env -> new DefaultSwaggerResourcesLocator(envConfigFunction.apply(env)));
    }

    public void configure(Environment environment, DropwizardResourceConfig resourceConfig) {

        final ViewMessageBodyWriter viewMessageBodyWriter = new ViewMessageBodyWriter(environment.metrics(), Collections.singletonList(new MustacheViewRenderer()));
        resourceConfig.register(viewMessageBodyWriter);
        resourceConfig.register(new SwaggerSerializers());
        resourceConfig.register(swaggerResourcesLocatorFactory.forEnvironment(environment));
    }
}
