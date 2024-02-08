package com.example.myapp.api;

import com.example.myapp.data.response.BusRouteSearchResponse;
import com.example.myapp.data.response.BusRouteStopDetailSearchResponse;
import com.example.myapp.data.response.BusRouteStopETASearchResponse;
import com.example.myapp.data.response.BusRouteStopSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BusRouteService {
    @GET("/v1/transport/kmb/route/")
    Call<BusRouteSearchResponse> searchBusRoute();

    @GET("/v1/transport/kmb/route-stop/{route}/{direction}/{service_type}")
    Call<BusRouteStopSearchResponse> searchRouteStopListByRoute(
            @Path("route") String route,
            @Path("direction") String direction,
            @Path("service_type") String serviceType
    );

    @GET("/v1/transport/kmb/stop/{stop}")
    Call<BusRouteStopDetailSearchResponse> searchBusRouteStopDetail(
            @Path("stop") String stop
    );

    @GET("/v1/transport/kmb/eta/{stop_id}/{route}/{service_type}")
    Call<BusRouteStopETASearchResponse> searchBusRouteStopETAInfo(
            @Path("stop_id") String stop_id,
            @Path("route") String route,
            @Path("service_type") String serviceType
    );
}
