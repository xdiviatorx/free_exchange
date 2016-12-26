package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 26.08.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

    @JsonProperty("id")
    int id;

    @JsonProperty("name")
    String name;

    @JsonProperty("display")
    int display;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDisplay() {
        return display;
    }
}
