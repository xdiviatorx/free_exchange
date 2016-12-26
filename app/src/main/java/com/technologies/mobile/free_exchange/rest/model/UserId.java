package com.technologies.mobile.free_exchange.rest.model;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 23.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserId {

    @JsonProperty("status")
    String status;

    @JsonProperty("uid")
    String uid;

    public String getUid() {
        return uid;
    }

    public String getStatus() {
        return status;
    }
}
