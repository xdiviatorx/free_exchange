package com.technologies.mobile.free_exchange.rest;

import com.technologies.mobile.free_exchange.rest.model.Resp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by diviator on 16.08.2016.
 */
public interface ExchangeClient {

    String apiKey = "07642cafbcfa448e20d9a9a6ac798355";

    @FormUrlEncoded
    @POST("API/GetVKGroupId")
    Call<Resp> getVkGroupId(@Field("APIKey") String apiKey);

}
