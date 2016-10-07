package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 07.10.2016.
 */

public class GetOffersByListResponse {

    @JsonProperty("RESPONSE")
    SubscribeOffers response;

    public SubscribeOffers getResponse() {
        return response;
    }
}
