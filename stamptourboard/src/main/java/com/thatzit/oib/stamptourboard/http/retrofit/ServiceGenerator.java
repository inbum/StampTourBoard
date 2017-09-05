package com.thatzit.oib.stamptourboard.http.retrofit;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thatzit.oib.stamptourboard.helper.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by inbum on 2017-03-09.
 */

public class ServiceGenerator {

    public static final String API_BOARD_BASE_URL = Constants.BOARD_API_URL;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();

    private static Retrofit.Builder boardBuilder =
            new Retrofit.Builder()
                    .baseUrl(API_BOARD_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    public static <S> S createBoardService( Class<S> serviceClass ) {
        Retrofit retrofit;

        boardBuilder.client(httpClient.build());
        retrofit = boardBuilder.build();

        return retrofit.create(serviceClass);
    }


}