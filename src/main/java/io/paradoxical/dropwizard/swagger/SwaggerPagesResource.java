package io.paradoxical.dropwizard.swagger;

import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class SwaggerPagesResource {

    public static final String DROPWIZARD_SWAGGER_ASSET_ROOT = "/dropwizard-swagger";

    @GET
    public IndexView handleSwagger() {
        return new IndexView("/dropwizard-swagger/swagger.mustache");
    }

    public String getApiBasePath(){
        return "/";
    }

    public static class IndexView extends View {
        protected IndexView(String templateName) {
            super(templateName);
        }
    }
}
