package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 31.10.2016.
 */

public class Photo {

    @JsonProperty("photo_75")
    String photo_75;

    @JsonProperty("photo_130")
    String photo_130;

    @JsonProperty("photo_604")
    String photo_604;

    @JsonProperty("photo_807")
    String photo_807;

    @JsonProperty("photo_1280")
    String photo_1280;

    @JsonProperty("photo_2560")
    String photo_2560;

    public String getPhoto75() {
        return photo_75;
    }

    public String getPhoto130() {
        return photo_130;
    }

    public String getPhoto604() {
        return photo_604;
    }

    public String getPhoto807() {
        return photo_807;
    }

    public String getPhoto1280() {
        return photo_1280;
    }

    public String getPhoto2560() {
        return photo_2560;
    }
}
