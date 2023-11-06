package com.example.reciclaapp;

import com.google.gson.annotations.SerializedName;

public class Cordinates {
    @SerializedName("lat")
    private String latitude;

    @SerializedName("lon")
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
