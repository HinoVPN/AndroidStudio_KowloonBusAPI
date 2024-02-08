package com.example.myapp.data.model;

import com.google.gson.annotations.SerializedName;

public class BusRouteInfo {
    @SerializedName("co")
    public String company;
    @SerializedName("route")
    public String route;
    @SerializedName("bound")
    public String bound;
    @SerializedName("service_type")
    public String serviceType;
    @SerializedName("orig_en")
    public String originEnglish;
    @SerializedName("orig_tc")
    public String originTraditionalChinese;
    @SerializedName("orig_sc")
    public String originSimplifiedChinese;
    @SerializedName("dest_en")
    public String destinationEnglish;
    @SerializedName("dest_tc")
    public String destinationTraditionalChinese;
    @SerializedName("dest_sc")
    public String destinationSimplifiedChinese;
    @SerializedName("data_timestamp")
    public String dataTimestamp;


    public BusRouteInfo(String company, String route, String bound, String serviceType, String originEnglish, String originTraditionalChinese, String originSimplifiedChinese, String destinationEnglish, String destinationTraditionalChinese, String destinationSimplifiedChinese, String dataTimestamp) {
        this.company = company;
        this.route = route;
        this.bound = bound;
        this.serviceType = serviceType;
        this.originEnglish = originEnglish;
        this.originTraditionalChinese = originTraditionalChinese;
        this.originSimplifiedChinese = originSimplifiedChinese;
        this.destinationEnglish = destinationEnglish;
        this.destinationTraditionalChinese = destinationTraditionalChinese;
        this.destinationSimplifiedChinese = destinationSimplifiedChinese;
        this.dataTimestamp = dataTimestamp;
    }
}