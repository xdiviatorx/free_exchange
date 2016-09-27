package com.technologies.mobile.free_exchange.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technologies.mobile.free_exchange.rest.model.AddMessageResponse;
import com.technologies.mobile.free_exchange.rest.model.AddResponse;
import com.technologies.mobile.free_exchange.rest.model.AddUserResponse;
import com.technologies.mobile.free_exchange.rest.model.Categories;
import com.technologies.mobile.free_exchange.rest.model.DialogMessagesResponse;
import com.technologies.mobile.free_exchange.rest.model.GetUserResponse;
import com.technologies.mobile.free_exchange.rest.model.ListDialogsResponse;
import com.technologies.mobile.free_exchange.rest.model.Search;
import com.technologies.mobile.free_exchange.rest.model.SearchResponse;
import com.technologies.mobile.free_exchange.rest.model.VkGroupIdResponse;
import com.technologies.mobile.free_exchange.rest.model.VkPostTemplateResponse;

import org.json.JSONArray;
import org.json.JSONObject;

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
    @POST("API/getUsersBy")
    Call<GetUserResponse> getUsersBy(@Field("by") JSONObject by, @Field("fields") JSONArray fields, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/AddUser")
    Call<AddUserResponse> addUser(@Field("vk_id") String vkId, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/GetVKGroupId")
    Call<VkGroupIdResponse> getVkGroupId(@Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/Search")
    Call<Search> findExchanges(@Field("ItemsGive") String itemsGive, @Field("ItemsGet") String itemsGet,
                               @Field("ItemsOffset") int offset, @Field("ItemsCount") int count,
                               @Field("Category") int category, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/GetCategoriesList")
    Call<Categories> getCategoriesList(@Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/GetVKPostTemplate")
    Call<VkPostTemplateResponse> getVkPostTemplate(@Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/Add")
    Call<AddResponse> addPost(@Field("id") String id,
                              @Field("ItemsPut") String itemsGive, @Field("ItemsGet") String itemsGet,
                              @Field("ContactsPM") String pm, @Field("ContactsPhone") String phone, @Field("Geo") String place,
                              @Field("Images") JSONArray JSONImagesArray, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/GetDialogs")
    Call<ListDialogsResponse> getDialogs(@Field("uid") String uid, @Field("offset") int offset, @Field("count") int count, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/GetDialogs")
    Call<ListDialogsResponse> getDialogs(@Field("uid") String uid, @Field("another_user") String anotherUser, @Field("offset") int offset, @Field("count") int count, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/GetDialogMessages")
    Call<DialogMessagesResponse> getDialogMessages(@Field("dialog_id") String dialogId, @Field("offset") int offset, @Field("count") int count, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/addMessage")
    Call<AddMessageResponse> addMessage(@Field("uid_to") String toId, @Field("uid_from") String fromId, @Field("text") String text, @Field("APIKey") String apiKey);
}
