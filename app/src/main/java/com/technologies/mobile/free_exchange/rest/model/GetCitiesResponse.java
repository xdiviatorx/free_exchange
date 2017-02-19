package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 19.02.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCitiesResponse {

    @JsonProperty("RESPONSE")
    City[] cities;

    public City[] getCities() {
        return cities;
    }
}
