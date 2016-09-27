package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 20.09.2016.
 */
public class ListDialogsResponse {

    @JsonProperty("RESPONSE")
    ListDialogs listDialogs;

    public ListDialogs getListDialogs() {
        return listDialogs;
    }
}
