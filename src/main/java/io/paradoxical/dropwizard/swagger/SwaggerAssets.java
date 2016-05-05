package io.paradoxical.dropwizard.swagger;

import io.paradoxical.dropwizard.bundles.assets.AssetsDefinition;
import io.paradoxical.dropwizard.swagger.bundles.SwaggerUIBundle;

public final class SwaggerAssets {
    private SwaggerAssets() {
    }

    public static final String DROPWIZARD_SWAGGER_ASSET_ROOT = "/dropwizard-swagger";

    public static final AssetsDefinition Assets =
        AssetsDefinition.builder()
                        .resourcePath(DROPWIZARD_SWAGGER_ASSET_ROOT)
                        .uriPath(DROPWIZARD_SWAGGER_ASSET_ROOT)
                        .assetsName(SwaggerUIBundle.class.getCanonicalName())
                        .build();
}
