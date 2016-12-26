package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 16.08.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {

    @JsonProperty("CODE")
    int code;

    @JsonProperty("TEXT")
    String result;

    public int getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }

}
