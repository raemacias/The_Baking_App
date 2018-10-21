package com.raemacias.thebakingapp.network;

import com.raemacias.thebakingapp.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private static final String END_POINT = "baking.json";
    private static retrofit2.Retrofit retrofit = null;

    public static retrofit2.Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .readTimeout(90, TimeUnit.SECONDS)
                    .connectTimeout(90, TimeUnit.SECONDS)
                    .writeTimeout(90, TimeUnit.SECONDS)
                    .cache(null);

            }

        return retrofit;
    }
}
