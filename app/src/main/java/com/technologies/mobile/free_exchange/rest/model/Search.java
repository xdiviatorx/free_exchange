package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 22.11.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Search {

    @JsonProperty("response")
    SearchResponse searchResponse;

    public SearchResponse getSearchResponse() {
        return searchResponse;
    }
}
