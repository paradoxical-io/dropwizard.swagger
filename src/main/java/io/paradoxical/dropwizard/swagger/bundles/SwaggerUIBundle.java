package io.paradoxical.dropwizard.swagger.bundles;

import io.dropwizard.setup.Environment;
import io.paradoxical.dropwizard.bundles.assets.AssetsDefinition;
import io.paradoxical.dropwizard.bundles.assets.AssetsDefinitionBundle;
import io.paradoxical.dropwizard.swagger.SwaggerAssets;
import io.paradoxical.dropwizard.swagger.SwaggerConfiguration;
import io.paradoxical.dropwizard.swagger.SwaggerUIConfigurator;
import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class SwaggerUIBundle extends AssetsDefinitionBundle {

    private final SwaggerUIConfigurator swaggerUIConfigurator;

    public SwaggerUIBundle(@NonNull final SwaggerUIConfigurator swaggerUIConfigurator) {
        this(SwaggerAssets.Assets, swaggerUIConfigurator);
    }

    public SwaggerUIBundle(@NonNull final AssetsDefinition assets, @NonNull final SwaggerUIConfigurator swaggerUIConfigurator) {
        super(assets);
        this.swaggerUIConfigurator = swaggerUIConfigurator;
    }

    //
    // Simplified common constructors
    //
    public SwaggerUIBundle(@NonNull @Nonnull final SwaggerConfiguration swaggerConfiguration) {
        this(SwaggerAssets.Assets, new SwaggerUIConfigurator(swaggerConfiguration));
    }

    public SwaggerUIBundle(@NonNull @Nonnull final Function<Environment, SwaggerConfiguration> envConfigFunction) {
        this(SwaggerAssets.Assets, new SwaggerUIConfigurator(envConfigFunction));
    }

    @Override
    public void run(final Environment environment) {
        super.run(environment);
        swaggerUIConfigurator.configure(environment, environment.jersey());
    }
}
