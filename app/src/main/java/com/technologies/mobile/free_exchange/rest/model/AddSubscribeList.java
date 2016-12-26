package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 05.10.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddSubscribeList {

    @JsonProperty("status")
    String status;

    @JsonProperty("list_id")
    String listId;

    public String getStatus() {
        return status;
    }

    public String getListId() {
        return listId;
    }
}
