package io.paradoxical.dropwizard.swagger.sample;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Test")
@AdminOnly
public class AdminTestResource {

    public AdminTestResource() {
    }

    @GET
    @Timed
    @ApiOperation(
        value = "says hello",
        response = Object.class
    )
    public Response sayHello(@QueryParam("name") String name) {
        return Response.ok(new Object(){
            public final String helloMessage = "Hello " + name;
        }).build();
    }
}
