package com.technologies.mobile.free_exchange.rest;

import com.technologies.mobile.free_exchange.rest.model.Result;
import com.technologies.mobile.free_exchange.rest.model.VkGroupId;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by diviator on 16.08.2016.
 */
public interface ExchangeClient {

    String apiKey = "07642cafbcfa448e20d9a9a6ac798355";

    @POST("/API/GetVKGroupId")
    Call<Result> getVkGroupId(@Query("APIKey") String apiKey);

}
