package com.example.myapp.data.response;

import com.example.myapp.data.model.BusRouteInfo;
import com.example.myapp.data.model.BusRouteStopETAInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusRouteStopETASearchResponse extends BaseResponse{
    @SerializedName("data")
    private List<BusRouteStopETAInfo> data;

    public List<BusRouteStopETAInfo> getData() {
        return data;
    }

    public void setData(List<BusRouteStopETAInfo> data) {
        this.data = data;
    }
}
