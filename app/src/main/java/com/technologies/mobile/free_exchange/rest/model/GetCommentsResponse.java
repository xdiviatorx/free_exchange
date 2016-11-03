package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 19.10.2016.
 */

public class GetCommentsResponse {

    @JsonProperty("RESPONSE")
    GetComments response;

    public GetComments getResponse() {
        return response;
    }
}
