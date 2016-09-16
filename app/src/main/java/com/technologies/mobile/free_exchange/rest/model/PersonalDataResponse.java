package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 01.09.2016.
 */
public class PersonalDataResponse {

    @JsonProperty("response")
    PersonalData[] personalDataArray;

    public PersonalData[] getPersonalDataArray() {
        return personalDataArray;
    }
}
