package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 04.10.2016.
 */

public class GetSubscribeListsResponse {

    @JsonProperty("RESPONSE")
    SubscribeLists response;

    public SubscribeLists getResponse() {
        return response;
    }
}
