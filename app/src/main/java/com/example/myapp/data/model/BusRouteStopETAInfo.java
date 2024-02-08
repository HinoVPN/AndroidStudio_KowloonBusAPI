package com.example.myapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class BusRouteStopETAInfo {
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
    @SerializedName("dest_tc")
    public String destTc;
    @SerializedName("dest_sc")
    public String destSc;
    @SerializedName("dest_en")
    public String destEn;
    @SerializedName("eta_seq")
    public String etaSeq;
    @SerializedName("eta")
    public String eta;
    @SerializedName("rmk_tc")
    public String rmkTc;
    @SerializedName("rmk_sc")
    public String rmkSc;
    @SerializedName("rmk_en")
    public String rmkEn;
    @SerializedName("data_timestamp")
    public String dataTimestamp;

}
