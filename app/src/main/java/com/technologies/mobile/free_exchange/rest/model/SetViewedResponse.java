package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 27.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SetViewedResponse {

    @JsonProperty("RESPONSE")
    String response;

    public String getResponse() {
        return response;
    }
}
