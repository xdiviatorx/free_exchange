package com.technologies.mobile.free_exchange.rest.model;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 16.08.2016.
 */
public class VkGroupIdResponse {
    @JsonProperty("ERROR")
    Error error;

    @JsonProperty("RESPONSE")
    Integer vkGroupId;

    public Integer getVkGroupId() {
        return vkGroupId;
    }

    public Error getError() {
        return error;
    }
}
