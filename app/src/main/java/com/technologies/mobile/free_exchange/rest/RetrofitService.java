package com.technologies.mobile.free_exchange.rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by diviator on 16.08.2016.
 */
public class RetrofitService {
    public static final String API_BASE_URL = "http://37.1.222.157";

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
