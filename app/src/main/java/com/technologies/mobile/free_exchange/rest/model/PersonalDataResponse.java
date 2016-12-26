package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 01.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalDataResponse {

    @JsonProperty("response")
    PersonalData[] personalDataArray;

    public PersonalData[] getPersonalDataArray() {
        return personalDataArray;
    }
}
