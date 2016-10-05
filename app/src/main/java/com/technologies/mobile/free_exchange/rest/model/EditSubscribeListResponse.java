package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 05.10.2016.
 */

public class EditSubscribeListResponse {

    @JsonProperty("RESPONSE")
    String response;

    public String getResponse() {
        return response;
    }
}
