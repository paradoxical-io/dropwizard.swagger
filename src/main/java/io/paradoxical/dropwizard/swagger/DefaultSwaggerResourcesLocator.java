package io.paradoxical.dropwizard.swagger;

import io.paradoxical.dropwizard.swagger.resources.SwaggerApiResource;
import io.paradoxical.dropwizard.swagger.resources.SwaggerUIResource;
import io.swagger.jaxrs.config.BeanConfig;
import lombok.NonNull;

import javax.ws.rs.Path;

@Path("/")
public class DefaultSwaggerResourcesLocator implements SwaggerResourcesLocator {

    private final BeanConfig swaggerConfig;

    public DefaultSwaggerResourcesLocator(@NonNull final BeanConfig swaggerConfig) {
        this.swaggerConfig = swaggerConfig;
    }

    @Override
    public SwaggerApiResource api() {
        return new SwaggerApiResource(swaggerConfig);
    }

    @Override
    public SwaggerUIResource ui() {
        return new SwaggerUIResource();
    }
}
