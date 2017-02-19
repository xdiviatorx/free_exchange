package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 19.02.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class City {

    public City() {
    }

    public City(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @JsonProperty("id")
    int id;

    @JsonProperty("name")
    String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
