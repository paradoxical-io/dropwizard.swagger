package io.paradoxical.dropwizard.swagger;

import io.dropwizard.setup.Environment;
import io.paradoxical.dropwizard.swagger.resources.SwaggerApiResource;
import io.paradoxical.dropwizard.swagger.resources.SwaggerUIResource;

import javax.ws.rs.Path;

@Path("/swagger")
public interface SwaggerResourcesLocator {
    @Path("/api")
    SwaggerApiResource api();

    @Path("/ui")
    SwaggerUIResource ui();

    interface Factory {
        SwaggerResourcesLocator forEnvironment(Environment environment);
    }
}
