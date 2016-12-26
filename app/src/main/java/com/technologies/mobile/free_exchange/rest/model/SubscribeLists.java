package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 04.10.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscribeLists {

    @JsonProperty("status")
    String status;

    @JsonProperty("count")
    String count;

    @JsonProperty("lists")
    SubscribeList[] subscribeLists;

    public String getStatus() {
        return status;
    }

    public String getCount() {
        return count;
    }

    public SubscribeList[] getSubscribeLists() {
        return subscribeLists;
    }
}
