package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 01.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalData {

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("uid")
    String id;

    @JsonProperty("photo_200")
    String photo200Url;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public String getPhoto200Url() {
        return photo200Url;
    }
}
