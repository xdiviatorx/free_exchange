package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 19.10.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

    @JsonProperty("id")
    String id;

    @JsonProperty("uid")
    String uid;

    @JsonProperty("offer")
    String offerId;

    @JsonProperty("text")
    String text;

    @JsonProperty("udata")
    User userData;

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getText() {
        return text;
    }

    public User getUserData() {
        return userData;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }
}
