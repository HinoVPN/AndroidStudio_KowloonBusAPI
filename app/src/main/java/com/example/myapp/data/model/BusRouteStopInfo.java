package com.example.myapp.data.model;

import com.google.gson.annotations.SerializedName;

public class BusRouteStopInfo {
    @SerializedName("co")
    public String company;
    @SerializedName("route")
    public String route;
    @SerializedName("bound")
    public String bound;
    @SerializedName("service_type")
    public String serviceType;
    @SerializedName("seq")
    public String seq;
    @SerializedName("stop")
    public String stop;
    @SerializedName("data_timestamp")
    public String dataTimestamp;

}
