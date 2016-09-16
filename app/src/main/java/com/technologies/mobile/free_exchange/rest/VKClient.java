package com.technologies.mobile.free_exchange.rest;

import com.technologies.mobile.free_exchange.rest.model.PersonalDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by diviator on 01.09.2016.
 */
public interface VKClient {

    @GET("method/users.get.json")
    Call<PersonalDataResponse> getUserData(@Query("user_ids") String id);

}
