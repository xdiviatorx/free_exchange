package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 27.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewMessagesResponse {

    @JsonProperty("RESPONSE")
    NewMessages response;

    public NewMessages getResponse() {
        return response;
    }
}
