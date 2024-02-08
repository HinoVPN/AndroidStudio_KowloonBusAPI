package com.example.myapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static RetrofitManager mInstance = new RetrofitManager();

    private BusRouteService busRouteService;

    private RetrofitManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://data.etabus.gov.hk")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        busRouteService = retrofit.create(BusRouteService.class);
    }

    public static BusRouteService getAPI() {
        return mInstance.busRouteService;
    }
}
