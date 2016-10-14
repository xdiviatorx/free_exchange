package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 11.10.2016.
 */

public class NewOffersByUid {

    @JsonProperty("count")
    int count;

    @JsonProperty("offers")
    SubscribeOffers[] lists;

    public int getCount() {
        return count;
    }

    public SubscribeOffers[] getLists() {
        return lists;
    }
}
