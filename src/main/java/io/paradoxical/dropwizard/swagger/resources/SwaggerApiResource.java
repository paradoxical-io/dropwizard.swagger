package io.paradoxical.dropwizard.swagger.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.config.FilterFactory;
import io.swagger.config.Scanner;
import io.swagger.core.filter.SpecFilter;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.JaxrsScanner;
import io.swagger.jaxrs.config.ReaderConfigUtils;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.models.Swagger;
import io.swagger.util.Yaml;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.godaddy.logging.LoggerFactory.getLogger;

/**
 * This is a slightly modified version of {@link ApiListingResource}
 * which takes in a swagger scanner instantiated PER http context
 *
 * Credits to @jakeswenson
 *
 * @implNote This contains 3 differences from {@link ApiListingResource}
 * in {@link #scan(Application, ServletConfig)} and
 * in {@link #process(Application, ServletConfig, HttpHeaders, UriInfo)}
 * See each method's doc comments for more details
 */
@Path("/")
public class SwaggerApiResource {
    private static final com.godaddy.logging.Logger LOGGER = getLogger(SwaggerApiResource.class);
    private static final Object initializationLock = new Object();
    private final BeanConfig swaggerConfig;
    private final ServletContext context;

    private final Swagger defaultSwagger;

    public SwaggerApiResource(
            @NonNull BeanConfig swaggerConfig,
            @NonNull @Context ServletContext context,
            Swagger defaultSwagger) {
        this.swaggerConfig = swaggerConfig;
        this.context = context;
        this.defaultSwagger = defaultSwagger;
    }

    public SwaggerApiResource(
            @NonNull BeanConfig swaggerConfig,
            @NonNull @Context ServletContext context) {
        this(swaggerConfig, context, null);
    }

    /**
     * Contains change #1 (to use the passed in {@link #swaggerConfig} as the {@link Scanner}
     * instead of using the static one normally set in {@link BeanConfig#setScan(boolean)})
     * from {@link ApiListingResource#scan(Application, ServletConfig)}
     * @param app used the same as in {@link ApiListingResource}
     * @param sc used the same as in {@link ApiListingResource}
     * @return the same as in {@link ApiListingResource}
     */
    protected synchronized Swagger scan(Application app, ServletConfig sc) {
        Swagger swagger = null;
        Scanner scanner = swaggerConfig; // Change #1a - don't use static factory
        LOGGER.debug("using scanner " + scanner);

        if (scanner != null) {
            SwaggerSerializers.setPrettyPrint(scanner.getPrettyPrint());
            swagger = (Swagger) context.getAttribute("swagger");

            // change #3
            // use default swagger if no context swagger
            if (swagger == null) {
                swagger = defaultSwagger;
            }

            Set<Class<?>> classes;
            if (scanner instanceof JaxrsScanner) {
                JaxrsScanner jaxrsScanner = (JaxrsScanner) scanner;
                classes = jaxrsScanner.classesFromContext(app, sc);
            }
            else {
                classes = scanner.classes();
            }

            if (classes != null) { // change #1b - simplify, since we know its a swagger config...
                Reader reader = new Reader(swagger, ReaderConfigUtils.getReaderConfig(context));
                swagger = reader.read(classes);
                swagger = swaggerConfig.configure(swagger);
                context.setAttribute("swagger", swagger);
            }
        }

        return swagger;
    }

    /**
     * Contains change #2 (a per app context init key) from {@link ApiListingResource#process(Application, ServletConfig, HttpHeaders, UriInfo)}
     * @param app used the same as in {@link ApiListingResource}
     * @param sc used the same as in {@link ApiListingResource}
     * @param headers used the same as in {@link ApiListingResource}
     * @param uriInfo used the same as in {@link ApiListingResource}
     * @return the same as in {@link ApiListingResource}
     */
    private Swagger process(
            Application app,
            ServletConfig sc,
            HttpHeaders headers,
            UriInfo uriInfo) {
        Swagger swagger = (Swagger) context.getAttribute("swagger");

        synchronized (initializationLock) {
            final String contextInitKey = "swagger-initialized"; // Change #2
            if (context.getAttribute(contextInitKey) == null) {
                swagger = scan(app, sc);
                context.setAttribute(contextInitKey, new Object());
            }
        }

        if (swagger != null) {
            SwaggerSpecFilter filterImpl = FilterFactory.getFilter();
            if (filterImpl != null) {
                SpecFilter f = new SpecFilter();
                swagger = f.filter(swagger, filterImpl, getQueryParams(uriInfo.getQueryParameters()), getCookies(headers),
                                   getHeaders(headers));
            }
        }
        return swagger;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, "application/yaml" })
    @ApiOperation(value = "The swagger definition in either JSON or YAML", hidden = true)
    @Path("/swagger.{type:json|yaml}")
    public Response getListing(
            @Context Application app,
            @Context ServletConfig sc,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo,
            @PathParam("type") String type) {
        if (StringUtils.isNotBlank(type) && type.trim().equalsIgnoreCase("yaml")) {
            return getListingYaml(app, sc, headers, uriInfo);
        }
        else {
            return getListingJson(app, sc, headers, uriInfo);
        }
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/swagger")
    @ApiOperation(value = "The swagger definition in JSON", hidden = true)
    public Response getListingJson(
            @Context Application app,
            @Context ServletConfig sc,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo) {
        Swagger swagger = process(app, sc, headers, uriInfo);

        if (swagger != null) {
            return Response.ok().entity(swagger).build();
        }
        else {
            return Response.status(404).build();
        }
    }

    @GET
    @Produces("application/yaml")
    @Path("/swagger")
    @ApiOperation(value = "The swagger definition in YAML", hidden = true)
    public Response getListingYaml(
            @Context Application app,
            @Context ServletConfig sc,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo) {
        Swagger swagger = process(app, sc, headers, uriInfo);
        try {
            if (swagger != null) {
                String yaml = Yaml.mapper().writeValueAsString(swagger);
                StringBuilder b = new StringBuilder();
                String[] parts = yaml.split("\n");
                for (String part : parts) {
                    b.append(part);
                    b.append("\n");
                }
                return Response.ok().entity(b.toString()).type("application/yaml").build();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(404).build();
    }

    protected Map<String, List<String>> getQueryParams(MultivaluedMap<String, String> params) {
        Map<String, List<String>> output = new HashMap<String, List<String>>();
        if (params != null) {
            for (String key : params.keySet()) {
                List<String> values = params.get(key);
                output.put(key, values);
            }
        }
        return output;
    }

    protected Map<String, String> getCookies(HttpHeaders headers) {
        Map<String, String> output = new HashMap<String, String>();
        if (headers != null) {
            for (String key : headers.getCookies().keySet()) {
                Cookie cookie = headers.getCookies().get(key);
                output.put(key, cookie.getValue());
            }
        }
        return output;
    }

    protected Map<String, List<String>> getHeaders(HttpHeaders headers) {
        Map<String, List<String>> output = new HashMap<String, List<String>>();
        if (headers != null) {
            for (String key : headers.getRequestHeaders().keySet()) {
                List<String> values = headers.getRequestHeaders().get(key);
                output.put(key, values);
            }
        }
        return output;
    }
}