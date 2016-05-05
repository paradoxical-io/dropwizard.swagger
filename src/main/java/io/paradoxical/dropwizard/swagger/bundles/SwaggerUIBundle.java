package io.paradoxical.dropwizard.swagger.bundles;

import io.dropwizard.setup.Environment;
import io.paradoxical.dropwizard.bundles.assets.AssetsDefinition;
import io.paradoxical.dropwizard.bundles.assets.AssetsDefinitionBundle;
import io.paradoxical.dropwizard.swagger.SwaggerAssets;
import io.paradoxical.dropwizard.swagger.SwaggerUIConfigurator;
import lombok.NonNull;

public class SwaggerUIBundle extends AssetsDefinitionBundle {

    private final SwaggerUIConfigurator swaggerUIConfigurator;

    public SwaggerUIBundle(@NonNull final SwaggerUIConfigurator swaggerUIConfigurator) {
        this(SwaggerAssets.Assets, swaggerUIConfigurator);
    }

    public SwaggerUIBundle(@NonNull final AssetsDefinition assets, @NonNull final SwaggerUIConfigurator swaggerUIConfigurator) {
        super(assets);
        this.swaggerUIConfigurator = swaggerUIConfigurator;
    }

    @Override
    public void run(final Environment environment) {
        super.run(environment);
        swaggerUIConfigurator.configure(environment, environment.jersey());
    }
}
