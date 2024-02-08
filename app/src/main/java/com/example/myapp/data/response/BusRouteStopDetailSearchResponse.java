package com.example.myapp.data.response;

import com.example.myapp.data.model.BusRouteStopDetail;
import com.google.gson.annotations.SerializedName;

public class BusRouteStopDetailSearchResponse extends BaseResponse {
    @SerializedName("data")
    private BusRouteStopDetail data;

    public BusRouteStopDetail getData() {
        return data;
    }
    public void setData(BusRouteStopDetail data) {
        this.data = data;
    }
}
