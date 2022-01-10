package com.roadstar.customerr.app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.roadstar.customerr.common.utils.ApiConstants.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RetrofitClientInstance {
    private static Retrofit retrofit;

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

//    public static Retrofit getClientForStringResponse() {
//
//        if (retrofit == null) {
//            retrofit=  new Retrofit.Builder()
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .baseUrl(BASE_URL)
//                    .build();
//        }
//        return retrofit;
//    }

    public static Retrofit getLiveTrackingClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retrofit;
    }

}
