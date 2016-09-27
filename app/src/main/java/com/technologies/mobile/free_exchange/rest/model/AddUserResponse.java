package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 23.09.2016.
 */
public class AddUserResponse {

    @JsonProperty("RESPONSE")
    UserId response;

    public UserId getResponse() {
        return response;
    }
}
