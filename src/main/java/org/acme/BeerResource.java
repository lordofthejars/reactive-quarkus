package org.acme;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("/beer")
public class BeerResource {

    @RestClient
    BeerService beerService;

    @GET
    @Path("/{beerA}/{beerB}")
    public Uni<JsonValue> compare(@PathParam("beerA") int beerA, @PathParam("beerB") int beerB) {
        Uni<JsonArray> beer1 = beerService.getBeer(beerA);
        Uni<JsonArray> beer2 = beerService.getBeer(beerB);

        return Uni.combine()
            .all()
            .unis(beer1, beer2)
            .with((b1, b2) -> this.compare(b1, b2));
    }

    private JsonValue compare(JsonArray beerA, JsonArray beerB) {
        JsonObject source = beerA.get(0).asJsonObject();
        JsonObject target = beerB.get(0).asJsonObject();

        String beerAName = source.getString("name");
        String beerBName = target.getString("name");

        double beerAAbv = source.getJsonNumber("abv").doubleValue();
        double beerBAbv = target.getJsonNumber("abv").doubleValue();

        return Json.createObjectBuilder()
            .add("source-name", beerAName)
            .add("target-name", beerBName)
            .add("source-abv", beerAAbv)
            .add("target-abv", beerBAbv)
            .build();
    }

    @GET
    public Multi<Beer> beers() {
        return Multi.createBy().repeating()
            .uni( 
                () -> new AtomicInteger(1),
                i -> beerService.getBeers(i.getAndIncrement())
            )
            .until(List::isEmpty)
            .onItem().<Beer>disjoint()
            .select().where(b -> b.getAbv() > 15.0);
    }

}
