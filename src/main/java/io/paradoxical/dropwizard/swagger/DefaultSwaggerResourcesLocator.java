package io.paradoxical.dropwizard.swagger;

import io.paradoxical.dropwizard.swagger.resources.SwaggerApiResource;
import io.paradoxical.dropwizard.swagger.resources.SwaggerUIResource;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Swagger;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

public class DefaultSwaggerResourcesLocator implements SwaggerResourcesLocator {

    @Getter
    @Nonnull
    private final BeanConfig swaggerConfig;

    @Getter
    @Nonnull
    private final Swagger defaultSwaggerModel;

    @Context
    @Getter
    @Setter
    @Nonnull
    @SuppressWarnings("NullableProblems")
    private ServletContext context;

    public DefaultSwaggerResourcesLocator(
            @NonNull @Nonnull final BeanConfig swaggerConfig,
            final Swagger defaultSwaggerModel) {
        this.swaggerConfig = swaggerConfig;
        this.defaultSwaggerModel = defaultSwaggerModel;
    }

    @Override
    public SwaggerApiResource api() {
        return new SwaggerApiResource(getSwaggerConfig(), getContext(), getDefaultSwaggerModel());
    }

    @Override
    public SwaggerUIResource ui() {
        return new SwaggerUIResource();
    }
}