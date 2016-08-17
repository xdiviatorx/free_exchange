package com.technologies.mobile.free_exchange.rest.model;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 16.08.2016.
 */
public class Resp {
    @Nullable
    @JsonProperty("ERROR")
    Error error;

    @Nullable
    @JsonProperty("RESPONSE")
    Integer vkGroupId;

    @Nullable
    public Integer getVkGroupId() {
        return vkGroupId;
    }

    @Nullable
    public Error getError() {
        return error;
    }
}
