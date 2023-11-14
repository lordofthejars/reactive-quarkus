package org.acme;

import java.math.BigDecimal;

import jakarta.json.bind.annotation.JsonbCreator;

public class PricedBeer {

    private String name;

    private BigDecimal price;

    private PricedBeer(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    @JsonbCreator
    public static PricedBeer of(String name, double price) {
        return new PricedBeer(name, new BigDecimal(price).setScale(2));
    }

    public static PricedBeer of(String name, BigDecimal price) {
        return new PricedBeer(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
