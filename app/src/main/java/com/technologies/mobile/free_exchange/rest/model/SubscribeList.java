package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;

/**
 * Created by diviator on 04.10.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscribeList {

    @JsonProperty("id")
    String listId;

    @JsonProperty("uid")
    String uid;

    @JsonProperty("ItemsGet")
    JSONArray itemsGet;

    @JsonProperty("ItemsGive")
    JSONArray itemsGive;

    @JsonProperty("category")
    JSONArray categories;

    @JsonProperty("notify")
    String isNewExchanges;

    @JsonProperty("notification")
    int notification;

    public String getListId() {
        return listId;
    }

    public String getUid() {
        return uid;
    }

    public JSONArray getItemsGet() {
        return itemsGet;
    }

    public JSONArray getItemsGive() {
        return itemsGive;
    }

    public JSONArray getCategories() {
        return categories;
    }

    public String getIsNewExchanges() {
        return isNewExchanges;
    }

    public int getNotification() {
        return notification;
    }
}
