package io.paradoxical.dropwizard.swagger.bundles;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Environment;
import io.paradoxical.dropwizard.swagger.SwaggerUIConfigurator;
import lombok.NonNull;

public class SwaggerUIBundle extends AssetsBundle {
    public static final String DROPWIZARD_SWAGGER_ASSET_ROOT = "/dropwizard-swagger";
    private final SwaggerUIConfigurator swaggerUIConfigurator;

    public SwaggerUIBundle(@NonNull final SwaggerUIConfigurator swaggerUIConfigurator) {
        super(DROPWIZARD_SWAGGER_ASSET_ROOT, DROPWIZARD_SWAGGER_ASSET_ROOT, null, SwaggerUIBundle.class.getCanonicalName());
        this.swaggerUIConfigurator = swaggerUIConfigurator;
    }

    @Override
    public void run(final Environment environment) {
        super.run(environment);
        swaggerUIConfigurator.configure(environment, environment.jersey().getResourceConfig());
    }
}
