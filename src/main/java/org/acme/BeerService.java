package org.acme;

import java.util.List;

import jakarta.json.JsonArray;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;

@Path("/v2")
@RegisterRestClient
public interface BeerService {

    @GET
    @Path("/beers")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<List<Beer>> getBeers(@QueryParam("page") int page);

    @GET
    @Path("/beers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<JsonArray> getBeer(@PathParam("id") int id);

}