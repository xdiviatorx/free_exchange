package com.technologies.mobile.free_exchange.rest.model;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 23.09.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("id")
    String id;

    @Nullable
    @JsonProperty("name")
    String name;

    @Nullable
    @JsonProperty("photo")
    String photo;

    @Nullable
    @JsonProperty("vk_id")
    String vkId;

    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getPhoto() {
        return photo;
    }

    @Nullable
    public String getVkId() {
        return vkId;
    }
}
