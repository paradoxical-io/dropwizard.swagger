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
import io.swagger.models.Swagger;
import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class SwaggerUIConfigurator implements AdminEnvironmentConfigurator {
    private final SwaggerResourcesLocator.Factory swaggerResourcesLocatorFactory;
    private final Swagger defaultSwaggerModel;

    @SuppressWarnings("WeakerAccess")
    public SwaggerUIConfigurator(
            @NonNull @Nonnull final SwaggerResourcesLocator.Factory swaggerResourcesLocatorFactory,
            final Swagger defaultSwaggerModel) {
        this.swaggerResourcesLocatorFactory = swaggerResourcesLocatorFactory;
        this.defaultSwaggerModel = defaultSwaggerModel;
    }

    @SuppressWarnings("WeakerAccess")
    public SwaggerUIConfigurator(@NonNull final SwaggerResourcesLocator.Factory swaggerResourcesLocatorFactory) {
        this(swaggerResourcesLocatorFactory, null);
    }

    @SuppressWarnings("WeakerAccess")
    public SwaggerUIConfigurator(@NonNull final SwaggerResourcesLocator swaggerResourcesLocator) {
        this((env, defaultSwagger) -> swaggerResourcesLocator);
    }

    @SuppressWarnings("unused")
    public SwaggerUIConfigurator(
            @NonNull @Nonnull final SwaggerConfiguration swaggerConfiguration,
            final Swagger defaultSwaggerModel) {
        this(new DefaultSwaggerResourcesLocator(swaggerConfiguration, defaultSwaggerModel));
    }

    @SuppressWarnings("unused")
    public SwaggerUIConfigurator(
            @NonNull @Nonnull final SwaggerConfiguration swaggerConfiguration) {
        this(swaggerConfiguration, null);
    }

    @SuppressWarnings("unused")
    public SwaggerUIConfigurator(
            @NonNull @Nonnull final Function<Environment, SwaggerConfiguration> envConfigFunction,
            final Swagger defaultSwaggerModel) {
        this(createDefaultFactory(envConfigFunction), defaultSwaggerModel);
    }

    @SuppressWarnings("unused")
    public SwaggerUIConfigurator(
            @NonNull @Nonnull final Function<Environment, SwaggerConfiguration> envConfigFunction) {
        this(envConfigFunction, null);
    }

    private static SwaggerResourcesLocator.Factory createDefaultFactory(
            final Function<Environment, SwaggerConfiguration> envConfigFunction) {
        return new SwaggerResourcesLocator.Factory() {
            @Override
            public SwaggerResourcesLocator create(final Environment env, final Swagger defaultSwagger) {

                final SwaggerConfiguration swaggerConfig = envConfigFunction.apply(env);

                return new DefaultSwaggerResourcesLocator(swaggerConfig, defaultSwagger);
            }
        };
    }

    public void configure(Environment environment, JerseyEnvironment jerseyEnvironment) {

        final ViewMessageBodyWriter viewMessageBodyWriter =
                new ViewMessageBodyWriter(environment.metrics(),
                                          ImmutableList.of(new MustacheViewRenderer()));

        jerseyEnvironment.register(viewMessageBodyWriter);
        jerseyEnvironment.register(new SwaggerSerializers());
        jerseyEnvironment.register(swaggerResourcesLocatorFactory.create(environment, defaultSwaggerModel));
    }


    @Override
    public void configure(final Configuration config, final AdminResourceEnvironment adminResourceEnvironment) {
        AdminEnvironmentConfigurator.forJersey(this::configure)
                                    .configure(config, adminResourceEnvironment);
    }
}
