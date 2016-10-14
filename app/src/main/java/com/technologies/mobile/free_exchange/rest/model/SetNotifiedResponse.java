package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 11.10.2016.
 */

public class SetNotifiedResponse {

    @JsonProperty("RESPONSE")
    String response;

    public String getResponse() {
        return response;
    }
}
