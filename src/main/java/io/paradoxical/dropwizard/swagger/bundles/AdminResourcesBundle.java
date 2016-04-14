package io.paradoxical.dropwizard.swagger.bundles;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Bundle;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.paradoxical.dropwizard.swagger.SwaggerAssets;
import io.paradoxical.dropwizard.swagger.SwaggerConfiguration;
import io.paradoxical.dropwizard.swagger.SwaggerUIConfigurator;
import lombok.Builder;
import lombok.Singular;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public final class AdminResourcesBundle implements Bundle {
    private static final CharMatcher wildcardMatcher = CharMatcher.anyOf("/*");

    private final ImmutableList<Object> registrations;
    private final String adminRootPath;
    private final SwaggerUIConfigurator addSwaggerUI;

    @Builder
    private AdminResourcesBundle(
            @Singular("register")
            final ImmutableList<Object> registrations,
            final String adminRootPath,
            @Nullable final SwaggerUIConfigurator swaggerUIConfigurator) {
        this.registrations = registrations == null ? ImmutableList.of() : registrations;
        this.adminRootPath = adminRootPath == null ? "/admin" : wildcardMatcher.trimTrailingFrom(adminRootPath);
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

        environment.admin().addServlet("admin-resources", jerseyContainerHolder.getContainer()).addMapping(adminRootPath + "/*");

        final JerseyEnvironment jerseyEnvironment = new JerseyEnvironment(jerseyContainerHolder, adminResourceConfig);

        if (addSwaggerUI != null) {
            addSwaggerUI.configure(environment, adminResourceConfig);
        }

        adminResourceConfig.logComponents();
    }

    private static class SwaggerUIAdminAssetsBundle extends AdminAssetsBundle {
        private SwaggerUIAdminAssetsBundle() {
            super(SwaggerAssets.Assets);
        }
    }
}
