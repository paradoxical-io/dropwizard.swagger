package io.paradoxical.dropwizard.swagger.resources;

import io.dropwizard.views.View;
import io.paradoxical.dropwizard.swagger.SwaggerAssets;
import lombok.Getter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This class is responsible for selecting the view to render for the swagger ui
 *
 * @apiNote the {@link #getDropwizardSwaggerViewResourcePath()} method can be overridden to change the view resource path.
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class SwaggerUIResource {

    @Getter
    private final String dropwizardSwaggerViewResourcePath = SwaggerAssets.DROPWIZARD_SWAGGER_ASSET_ROOT + "/swagger.mustache";

    @GET
    public View handleSwagger() {
        return new View(getDropwizardSwaggerViewResourcePath()) {};
    }
}
