package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 19.10.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetComments {

    @JsonProperty("count")
    String count;

    @JsonProperty("comments")
    Comment[] comments;

    public String getCount() {
        return count;
    }

    public Comment[] getComments() {
        return comments;
    }
}
