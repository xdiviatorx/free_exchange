package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 23.09.2016.
 */
public class GetUserResponse {

    @JsonProperty("RESPONSE")
    Users response;

    public Users getResponse() {
        return response;
    }
}
