package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 09.09.2016.
 */
public class AddResponse {

    @JsonProperty("RESPONSE")
    String response;

    @JsonProperty("ERROR")
    Error error;

    public String getResponse() {
        return response;
    }

    public Error getError() {
        return error;
    }
}
