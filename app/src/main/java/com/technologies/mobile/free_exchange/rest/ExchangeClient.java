package com.technologies.mobile.free_exchange.rest;

import com.technologies.mobile.free_exchange.rest.model.Search;
import com.technologies.mobile.free_exchange.rest.model.SearchResponse;
import com.technologies.mobile.free_exchange.rest.model.VkGroupIdResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by diviator on 16.08.2016.
 */
public interface ExchangeClient {

    String apiKey = "07642cafbcfa448e20d9a9a6ac798355";

    @FormUrlEncoded
    @POST("API/GetVKGroupId")
    Call<VkGroupIdResponse> getVkGroupId(@Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/Search")
    Call<Search> findExchanges(@Field("ItemsGive") String itemsGive, @Field("ItemsGet") String itemsGet,
                               @Field("ItemsOffset") int offset, @Field("ItemsCount") int count,
                               @Field("Category") int category, @Field("APIKey") String apiKey);
}
