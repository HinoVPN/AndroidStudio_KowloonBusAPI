package com.example.myapp.data.response;

import com.example.myapp.data.model.BusRouteStopInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusRouteStopSearchResponse extends BaseResponse {

    @SerializedName("data")
    private List<BusRouteStopInfo> data;

    public List<BusRouteStopInfo> getData() {
        return data;
    }

    public void setData(List<BusRouteStopInfo> data) {
        this.data = data;
    }
}
