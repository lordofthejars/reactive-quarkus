package org.acme;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestStreamElementType;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;

@Path("/beer")
public class BeerResource {

    @RestClient
    BeerService beerService;

    @GET
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Blocking
    public Multi<Beer> beers() {
        return Multi.createBy().repeating()
            .supplier( 
                () -> new AtomicInteger(1),
                i -> beerService.getBeers(i.getAndIncrement())
            )
            .until(List::isEmpty)
            .emitOn(Infrastructure.getDefaultWorkerPool())
            .onItem().<Beer>disjoint()
            .select().where(b -> b.getAbv() > 15.0);
    }

}
