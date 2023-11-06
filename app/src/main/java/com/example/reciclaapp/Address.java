package com.example.reciclaapp;

import com.google.gson.annotations.SerializedName;

public class Address {
    private String road;
    private String village;
    private String state_district;
    private String state;
    private String postcode;
    private String country;
    private String county;
    @SerializedName("country_code")
    private String countryCode;

    public String getRoad() {
        return road;
    }

    public String getVillage() {
        return village;
    }

    public String getStateDistrict() {
        return state_district;
    }

    public String getState() {
        return state;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }
    public String getCounty() {return county;}
}

