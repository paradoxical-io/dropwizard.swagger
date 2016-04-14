package io.paradoxical.dropwizard.swagger.bundles;

import io.dropwizard.assets.AssetsBundle;
import io.paradoxical.dropwizard.swagger.AssetsDefinition;

public class AssetsDefinitionBundle extends AssetsBundle {
    public AssetsDefinitionBundle(final AssetsDefinition assetsDefinition) {
        super(assetsDefinition.resourcePath(),
              assetsDefinition.uriPath(),
              assetsDefinition.indexFile(),
              assetsDefinition.assetsName());
    }
}
