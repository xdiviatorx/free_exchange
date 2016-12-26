package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 20.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListDialogs {

    @JsonProperty("dialogs")
    Dialog[] dialogs;

    public Dialog[] getDialogs() {
        return dialogs;
    }
}
