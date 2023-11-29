package com.greencarson.reciclaapp;

import com.google.gson.annotations.SerializedName;
/**
 * La clase Address encapsula información sobre una dirección, incluyendo detalles como la carretera,
 * la aldea, el distrito del estado, el estado, el código postal, el país, el condado y el código del país.
 */
public class Address {
    private String road;               // Nombre de la carretera
    private String village;            // Nombre de la aldea
    private String state_district;      // Distrito del estado
    private String state;               // Estado
    private String postcode;           // Código postal
    private String country;            // País
    private String county;             // Condado
    @SerializedName("country_code")
    private String countryCode;        // Código del país

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

