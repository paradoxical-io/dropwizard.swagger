package io.paradoxical.dropwizard.swagger;

import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import io.paradoxical.dropwizard.swagger.resources.SwaggerApiResource;
import io.paradoxical.dropwizard.swagger.resources.SwaggerUIResource;
import io.swagger.jaxrs.config.BeanConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

public class DefaultSwaggerResourcesLocator implements SwaggerResourcesLocator {

    @Getter
    private final BeanConfig swaggerConfig;

    @Context
    @Getter
    @Setter
    private ServletContext context;

    public DefaultSwaggerResourcesLocator(@NonNull final BeanConfig swaggerConfig) {
        this.swaggerConfig = swaggerConfig;
    }

    @Override
    public SwaggerApiResource api() {
        return new SwaggerApiResource(getSwaggerConfig(), getContext());
    }

    @Override
    public SwaggerUIResource ui() {
        return new SwaggerUIResource();
    }
}