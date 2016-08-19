package com.technologies.mobile.free_exchange.rest.model;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diviator on 17.08.2016.
 */
public class SearchResponse {

    @JsonProperty("count")
    Integer count;

    @JsonProperty("items")
    SearchExtraditionItem[] searchExtraditionItems;

    public Integer getCount() {
        return count;
    }

    public SearchExtraditionItem[] getSearchExtraditionItems() {
        return searchExtraditionItems;
    }
}
