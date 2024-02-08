package com.example.myapp.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseResponse {
    @SerializedName("type")
    private String type;
    @SerializedName("version")
    private String version;
    @SerializedName("generated_timestamp")
    private String generated_timestamp;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGeneratedTimestamp() {
        return generated_timestamp;
    }

    public void setGeneratedTimestamp(String generated_timestamp) {
        this.generated_timestamp = generated_timestamp;
    }

}
