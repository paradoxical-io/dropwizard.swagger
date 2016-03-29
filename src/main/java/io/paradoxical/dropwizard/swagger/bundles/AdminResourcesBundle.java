package io.paradoxical.dropwizard.swagger.bundles;

import com.google.common.collect.ImmutableList;
import io.dropwizard.Bundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.paradoxical.dropwizard.swagger.SwaggerUIConfigurator;
import io.paradoxical.dropwizard.swagger.bundles.AdminAssetsBundle;
import io.paradoxical.dropwizard.swagger.resources.SwaggerUIResource;
import lombok.Builder;
import lombok.Singular;
import org.glassfish.jersey.servlet.ServletContainer;

@SuppressWarnings("unused")
public final class AdminResourcesBundle implements Bundle {

    @Singular
    private final ImmutableList<Object> registrations;
    private final String adminRootPath;
    private final SwaggerUIConfigurator addSwaggerUI;

    @Builder
    private AdminResourcesBundle(
            final ImmutableList<Object> registrations,
            final String adminRootPath,
            final SwaggerUIConfigurator swaggerUIConfigurator) {
        this.registrations = registrations == null ? ImmutableList.of() : registrations;
        this.adminRootPath = adminRootPath == null ? "/admin/*" : adminRootPath;
        this.addSwaggerUI = swaggerUIConfigurator;
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        if (addSwaggerUI != null) {
            bootstrap.addBundle(new SwaggerUIAdminAssetsBundle());
        }
    }

    @Override
    public void run(final Environment environment) {
        final DropwizardResourceConfig adminResourceConfig = new DropwizardResourceConfig(environment.metrics());

        final JerseyContainerHolder jerseyContainerHolder = new JerseyContainerHolder(new ServletContainer(adminResourceConfig));

        registrations.forEach(adminResourceConfig::register);

        environment.admin().addServlet("admin-resources", jerseyContainerHolder.getContainer()).addMapping(adminRootPath);

        final JerseyEnvironment jerseyEnvironment = new JerseyEnvironment(jerseyContainerHolder, adminResourceConfig);

        if (addSwaggerUI != null) {
            addSwaggerUI.configure(environment, adminResourceConfig);
        }
    }

    private static class SwaggerUIAdminAssetsBundle extends AdminAssetsBundle {
        public SwaggerUIAdminAssetsBundle() {
            super(SwaggerUIBundle.DROPWIZARD_SWAGGER_ASSET_ROOT, SwaggerUIBundle.DROPWIZARD_SWAGGER_ASSET_ROOT);
        }
    }
}
