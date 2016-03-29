package io.paradoxical.dropwizard.swagger;

import io.dropwizard.setup.Environment;
import io.paradoxical.dropwizard.swagger.resources.SwaggerApiResource;
import io.paradoxical.dropwizard.swagger.resources.SwaggerUIResource;

import javax.ws.rs.Path;

public interface SwaggerResourcesLocator {
    @Path("/")
    SwaggerApiResource api();

    @Path("/swagger")
    SwaggerUIResource ui();

    interface Factory {
        SwaggerResourcesLocator forEnvironment(Environment environment);
    }
}
