package org.acme;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.common.annotation.Blocking;

@Path("/v2")
@RegisterRestClient
public interface BeerService {

    @GET
    @Path("/beers")
    @Produces(MediaType.APPLICATION_JSON)
    List<Beer> getBeers(@QueryParam("page") int page);

}