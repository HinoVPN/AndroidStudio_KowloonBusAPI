package com.example.myapp.data.response;

import com.example.myapp.data.model.BusRouteInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusRouteSearchResponse extends BaseResponse {

    @SerializedName("data")
    private List<BusRouteInfo> data;

    public List<BusRouteInfo> getData() {
        return data;
    }

    public void setData(List<BusRouteInfo> data) {
        this.data = data;
    }
}
