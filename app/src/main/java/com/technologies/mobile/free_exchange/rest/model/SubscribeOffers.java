package com.technologies.mobile.free_exchange.rest.model;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 07.10.2016.
 */

public class SubscribeOffers {

    @Nullable
    @JsonProperty("list_id")
    String listId;

    @JsonProperty("count")
    Integer count;

    @JsonProperty("offers")
    SearchExtraditionItem[] offers;

    public String getListId() {
        return listId;
    }

    public SearchExtraditionItem[] getOffers() {
        return offers;
    }

    public Integer getCount() {
        return count;
    }

}
