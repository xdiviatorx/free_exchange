package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 18.08.2016.
 */
public class Search {

    @JsonProperty("response")
    SearchResponse searchResponse;

    public SearchResponse getSearchResponse() {
        return searchResponse;
    }
}
