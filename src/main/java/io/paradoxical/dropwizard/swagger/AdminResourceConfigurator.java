package io.paradoxical.dropwizard.swagger;

import io.dropwizard.Bundle;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.ViewRenderer;
import io.dropwizard.views.mustache.MustacheViewRenderer;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import lombok.Getter;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminResourceConfigurator implements Bundle {
    private final List<Object> registrations;
    private final String adminRootPath;

    @Getter
    private DropwizardResourceConfig adminResourceConfig;

    @Getter
    private JerseyEnvironment jerseyEnvironment;

    @Getter
    private JerseyContainerHolder jerseyContainerHolder;

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

        jerseyContainerHolder = new JerseyContainerHolder(new ServletContainer(adminResourceConfig));

        registrations.forEach(adminResourceConfig::register);

        environment.admin().addServlet("admin-resources", jerseyContainerHolder.getContainer()).addMapping(adminRootPath);

        jerseyEnvironment = new JerseyEnvironment(jerseyContainerHolder, adminResourceConfig);
    }

    public void enableSwagger(Environment environment, BeanConfig config) {
        enableSwagger(environment, new SwaggerPagesResource(), new SwaggerApiResource(config));
    }

    public <T extends SwaggerPagesResource, Y extends SwaggerApiResource> void enableSwagger(Environment environment, T page, Y api) {
        adminResourceConfig.register(page);

        adminResourceConfig.register(api);

        registerSwaggerDependencies(environment);
    }

    public void registerSwaggerDependencies(Environment environment) {
        final ViewMessageBodyWriter viewMessageBodyWriter = new ViewMessageBodyWriter(environment.metrics(), Collections.singletonList(new MustacheViewRenderer()));

        adminResourceConfig.register(viewMessageBodyWriter);

        adminResourceConfig.register(new SwaggerSerializers());
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
