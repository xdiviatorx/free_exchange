package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 05.10.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EditSubscribeList {

    @JsonProperty("status")
    String status;

    public String getStatus() {
        return status;
    }
}
