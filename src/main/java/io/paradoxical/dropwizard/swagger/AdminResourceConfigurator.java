package io.paradoxical.dropwizard.swagger;

import io.dropwizard.Bundle;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.ViewRenderer;
import io.dropwizard.views.mustache.MustacheViewRenderer;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminResourceConfigurator implements Bundle {
    private final List<Object> registrations;
    private final String adminRootPath;

    private DropwizardResourceConfig adminResourceConfig;

    private AdminResourceConfigurator(
            final List<Object> registrations,
            final String adminRootPath) {
        this.registrations = registrations;
        this.adminRootPath = adminRootPath;
    }

    public static AdminResourceConfiguratorBuilder builder() {
        return new AdminResourceConfiguratorBuilder();
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(final Environment environment) {
        adminResourceConfig = new DropwizardResourceConfig(environment.metrics());

        JerseyContainerHolder adminContainerHolder = new JerseyContainerHolder(new ServletContainer(adminResourceConfig));

        registrations.forEach(adminResourceConfig::register);

        environment.admin().addServlet("admin-resources", adminContainerHolder.getContainer()).addMapping(adminRootPath);
    }

    public void enableSwagger(Environment environment, BeanConfig config) {
        register(new SwaggerPagesResource());

        register(new SwaggerApiResource(config));

        final ViewMessageBodyWriter viewMessageBodyWriter = new ViewMessageBodyWriter(environment.metrics(), Collections.singletonList(new MustacheViewRenderer()));

        register(viewMessageBodyWriter);

        register(new SwaggerSerializers());
    }

    public void register(Object object) {
        if (adminResourceConfig == null) {
            throw new RuntimeException("Can not register until after bootstrap phase is complete!");
        }

        adminResourceConfig.register(object);
    }

    public static class AdminResourceConfiguratorBuilder {
        private List<Object> registrations = new ArrayList<>();

        private List<ViewRenderer> viewRenderers = new ArrayList<>();

        private String adminRoot = "/admin/*";

        public AdminResourceConfigurator build() {
            return new AdminResourceConfigurator(registrations, adminRoot);
        }

        public AdminResourceConfiguratorBuilder register(Object object) {
            registrations.add(object);

            return this;
        }

        public AdminResourceConfiguratorBuilder adminRootPath(String path) {
            adminRoot = path;

            return this;
        }
    }
}
