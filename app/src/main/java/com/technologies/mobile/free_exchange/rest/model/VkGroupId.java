package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 16.08.2016.
 */
public class VkGroupId {
    @JsonProperty("id")
    int id;

    @JsonProperty("CODE")
    int code;

    @JsonProperty("TEXT")
    String result;

    public int getId() {
        return id;
    }

    public int getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }
}
