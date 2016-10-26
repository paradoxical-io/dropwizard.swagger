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

@Path("/hello-admin")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Admin Test")
@AdminOnly
public class AdminTestResource {

    public AdminTestResource() {
    }

    @GET
    @Timed
    @ApiOperation(
        value = "says hello admin",
        response = Object.class
    )
    public Response sayHello(@QueryParam("name") String name) {
        return Response.ok(new Object(){
            public final String helloMessage = "Hello admin " + name;
        }).build();
    }
}
