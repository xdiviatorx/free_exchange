package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 23.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserResponse {

    @JsonProperty("RESPONSE")
    Users response;

    public Users getResponse() {
        return response;
    }
}
