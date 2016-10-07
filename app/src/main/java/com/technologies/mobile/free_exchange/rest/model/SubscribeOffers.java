package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 07.10.2016.
 */

public class SubscribeOffers {

    @JsonProperty("count")
    Integer count;

    @JsonProperty("offers")
    SearchExtraditionItem[] offers;

    public SearchExtraditionItem[] getOffers() {
        return offers;
    }

    public Integer getCount() {
        return count;
    }

}
