package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;

/**
 * Created by diviator on 20.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dialog {

    @JsonProperty("id")
    int dialog_id;

    @JsonProperty("users")
    User[] users;

    public int getDialog_id() {
        return dialog_id;
    }

    public User[] getUsers() {
        return users;
    }
}
