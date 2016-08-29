package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 26.08.2016.
 */
public class CategoriesResponse {

    @JsonProperty("cats")
    Category[] categories;

    public Category[] getCategories() {
        return categories;
    }
}
