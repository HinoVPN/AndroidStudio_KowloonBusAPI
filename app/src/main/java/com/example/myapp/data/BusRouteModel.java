package com.example.myapp.data;

import androidx.annotation.NonNull;

import com.example.myapp.api.BusRouteService;
import com.example.myapp.api.RetrofitManager;
import com.example.myapp.data.model.BusRouteInfo;
import com.example.myapp.data.model.BusRouteStopDetail;
import com.example.myapp.data.model.BusRouteStopETAInfo;
import com.example.myapp.data.response.BusRouteSearchResponse;
import com.example.myapp.data.model.BusRouteStopInfo;
import com.example.myapp.data.response.BusRouteStopDetailSearchResponse;
import com.example.myapp.data.response.BusRouteStopETASearchResponse;
import com.example.myapp.data.response.BusRouteStopSearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class BusRouteModel{

    private BusRouteService busRouteService = RetrofitManager.getAPI();

    public void searchBusRoute(final onDataReadyCallback callback) {
        busRouteService.searchBusRoute()
                .enqueue(new Callback<BusRouteSearchResponse>() {
                    @Override
                    public void onResponse(Call<BusRouteSearchResponse> call, retrofit2.Response<BusRouteSearchResponse> response) {
                        callback.onDataReady(response.body().getData());
                    }

                    @Override
                    public void onFailure(@NonNull Call<BusRouteSearchResponse> call, @NonNull Throwable t) {
                        // TODO: error handle
                    }
                });
    }

    public interface onDataReadyCallback {
        void onDataReady(List<BusRouteInfo> data);
    }

    public void searchRouteStopListByRoute(String route, String direction, String service_type, final onRouteStopDataReadyCallback callback) {
        busRouteService.searchRouteStopListByRoute(route,direction,service_type)
                .enqueue(new Callback<BusRouteStopSearchResponse>() {
                    @Override
                    public void onResponse(Call<BusRouteStopSearchResponse> call, retrofit2.Response<BusRouteStopSearchResponse> response) {
                        callback.onDataReady(response.body().getData());
                    }

                    @Override
                    public void onFailure(@NonNull Call<BusRouteStopSearchResponse> call, @NonNull Throwable t) {
                        // TODO: error handle
                    }
                });
    }

    public interface onRouteStopDataReadyCallback {
        void onDataReady(List<BusRouteStopInfo> data);
    }


    public void searchBusRouteStopDetail(String stop, final onRouteStopDetailDataReadyCallback callback) {
        busRouteService.searchBusRouteStopDetail(stop)
                .enqueue(new Callback<BusRouteStopDetailSearchResponse>() {
                    @Override
                    public void onResponse(Call<BusRouteStopDetailSearchResponse> call, retrofit2.Response<BusRouteStopDetailSearchResponse> response) {
                        callback.onDataReady(response.body().getData());
                    }

                    @Override
                    public void onFailure(@NonNull Call<BusRouteStopDetailSearchResponse> call, @NonNull Throwable t) {
                        // TODO: error handle
                    }
                });
    }

    public interface onRouteStopDetailDataReadyCallback {
        void onDataReady(BusRouteStopDetail data);
    }

    public void searchBusRouteStopETAInfo(String stop,String route,  String service_type, final onRouteStopETAInfoDataReadyCallback callback) {
        busRouteService.searchBusRouteStopETAInfo(stop,route,service_type)
                .enqueue(new Callback<BusRouteStopETASearchResponse>() {
                    @Override
                    public void onResponse(Call<BusRouteStopETASearchResponse> call, retrofit2.Response<BusRouteStopETASearchResponse> response) {
                        callback.onDataReady(response.body().getData());
                    }

                    @Override
                    public void onFailure(@NonNull Call<BusRouteStopETASearchResponse> call, @NonNull Throwable t) {
                        // TODO: error handle
                    }
                });
    }

    public interface onRouteStopETAInfoDataReadyCallback {
        void onDataReady(List<BusRouteStopETAInfo> data);
    }




}
