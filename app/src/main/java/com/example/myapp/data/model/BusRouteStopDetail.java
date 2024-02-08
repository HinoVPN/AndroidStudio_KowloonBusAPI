package com.example.myapp.data.model;

import com.google.gson.annotations.SerializedName;

public class BusRouteStopDetail {
    @SerializedName("stop")
    public String stop;
    @SerializedName("name_tc")
    public String nameTc;
    @SerializedName("name_en")
    public String nameEn;
    @SerializedName("name_sc")
    public String nameSc;
    @SerializedName("lat")
    public double latitude;
    @SerializedName("long")
    public double longitude;
    @SerializedName("data_timestamp")
    public String dataTimestamp;
}
