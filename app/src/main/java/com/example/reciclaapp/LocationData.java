package com.example.reciclaapp;
import com.google.gson.annotations.SerializedName;
public class LocationData {
    @SerializedName("address")
    private Address address;

    public Address getAddress() {
        return address;
    }
}
