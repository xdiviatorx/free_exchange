package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 11.10.2016.
 */

public class NewOffersByUidResponse {

    @JsonProperty("RESPONSE")
    NewOffersByUid response;

    public NewOffersByUid getResponse() {
        return response;
    }
}
