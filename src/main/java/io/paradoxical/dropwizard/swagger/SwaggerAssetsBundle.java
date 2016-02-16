package io.paradoxical.dropwizard.swagger;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.mustache.MustacheViewRenderer;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import java.util.Collections;

public class SwaggerAssetsBundle extends AssetsBundle {
    private final ApiProvider apiProvider;
    private final ConfigProvider configProvider;
    private final SwaggerPagesResource page;

    public SwaggerAssetsBundle(ApiProvider apiProvider, SwaggerPagesResource page) {
        super(SwaggerPagesResource.DROPWIZARD_SWAGGER_ASSET_ROOT, SwaggerPagesResource.DROPWIZARD_SWAGGER_ASSET_ROOT);

        this.configProvider = null;
        this.apiProvider = apiProvider;
        this.page = page;
    }

    public SwaggerAssetsBundle(ConfigProvider configProvider, SwaggerPagesResource page) {
        super(SwaggerPagesResource.DROPWIZARD_SWAGGER_ASSET_ROOT, SwaggerPagesResource.DROPWIZARD_SWAGGER_ASSET_ROOT);
        this.configProvider = configProvider;

        this.apiProvider = null;

        this.page = page;
    }

    public SwaggerAssetsBundle(ConfigProvider configProvider) {
        super(SwaggerPagesResource.DROPWIZARD_SWAGGER_ASSET_ROOT, SwaggerPagesResource.DROPWIZARD_SWAGGER_ASSET_ROOT);
        this.configProvider = configProvider;

        this.apiProvider = null;

        this.page = new SwaggerPagesResource();
    }

    @Override
    public void run(final Environment environment) {
        super.run(environment);

        if (apiProvider == null && configProvider == null) {
            throw new RuntimeException("Require either api provider or config provider");
        }

        SwaggerApiProvider apiResource = apiProvider == null ? new SwaggerApiResource(configProvider.apply(environment)) : apiProvider.apply(environment);

        final ViewMessageBodyWriter viewMessageBodyWriter = new ViewMessageBodyWriter(environment.metrics(), Collections.singletonList(new MustacheViewRenderer()));

        environment.jersey().register(viewMessageBodyWriter);

        environment.jersey().register(new SwaggerSerializers());

        environment.jersey().register(page);

        environment.jersey().register(apiResource);

    }
}

