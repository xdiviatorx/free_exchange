package com.technologies.mobile.free_exchange.rest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by diviator on 01.09.2016.
 */
public class VKRetrofitService {
    public static final String API_BASE_URL = "https://api.vk.com";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .readTimeout(24, TimeUnit.HOURS)
            .connectTimeout(24, TimeUnit.HOURS);

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
