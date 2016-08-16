package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 16.08.2016.
 */
public class Result {
    @JsonProperty("ERROR")
    VkGroupId vkGroupId;

    public VkGroupId getVkGroupId() {
        return vkGroupId;
    }
}
