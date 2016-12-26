package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 11.10.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
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
