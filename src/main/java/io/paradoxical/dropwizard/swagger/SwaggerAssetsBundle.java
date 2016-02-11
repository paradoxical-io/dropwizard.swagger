package io.paradoxical.dropwizard.swagger;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.mustache.MustacheViewRenderer;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import java.util.Arrays;
import java.util.function.Function;

public class SwaggerAssetsBundle extends AssetsBundle {
    private final Function<Environment, BeanConfig> configProvider;

    public SwaggerAssetsBundle() {
        this(null);
    }

    public SwaggerAssetsBundle(Function<Environment, BeanConfig> configProvider) {
        super(SwaggerPagesResource.DROPWIZARD_SWAGGER_ASSET_ROOT, SwaggerPagesResource.DROPWIZARD_SWAGGER_ASSET_ROOT);

        this.configProvider = configProvider;
    }

    @Override
    public void run(final Environment environment) {
        super.run(environment);

        if (configProvider != null) {
            final ViewMessageBodyWriter viewMessageBodyWriter = new ViewMessageBodyWriter(environment.metrics(), Arrays.asList(new MustacheViewRenderer()));

            environment.jersey().register(viewMessageBodyWriter);

            environment.jersey().register(new SwaggerSerializers());

            environment.jersey().register(new SwaggerPagesResource());

            environment.jersey().register(new SwaggerApiResource(configProvider.apply(environment)));
        }
    }
}

