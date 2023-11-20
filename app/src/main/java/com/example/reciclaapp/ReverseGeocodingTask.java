package com.example.reciclaapp;
import android.os.AsyncTask;
import org.osmdroid.util.GeoPoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

/**
 * Tarea asincrónica para realizar la geocodificación inversa y obtener información de ubicación a partir de coordenadas.
 */
public class ReverseGeocodingTask extends AsyncTask<GeoPoint, Void, List<String>> {
    GeoPoint startPoint = null;
    Double Latitud = null;
    Double Longitud = null;
    // URL de la API de Nominatim para la geocodificación inversa
    public String nominatimApiUrl = "https://nominatim.openstreetmap.org/reverse?format=jsonv2";
    @Override
    protected List<String> doInBackground(GeoPoint... geoPoints) {
        List<String> locationInfoList = new ArrayList<>();

        if (geoPoints.length == 0) {
            return locationInfoList;
        }

        startPoint = geoPoints[0];
        Latitud = startPoint.getLatitude();
        Longitud = startPoint.getLongitude();

        try {
            URL url = new URL(nominatimApiUrl + "&lat=" + Latitud + "&lon=" + Longitud);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                inputStream.close();
                connection.disconnect();


                locationInfoList = processReverseGeocodingResponse(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationInfoList;
    }
    /**
     * Procesa la respuesta de la geocodificación inversa y extrae la información relevante.
     */
    private List<String> processReverseGeocodingResponse(String response) {

        List<String> result = new ArrayList<>();

        try {

            Gson gson = new Gson();
            LocationData locationData = gson.fromJson(response, LocationData.class);

            if (locationData != null) {
                Address address = locationData.getAddress();

                if (address != null) {

                    String road = address.getRoad();
                    String village = address.getVillage();
                    String stateDistrict = (address.getStateDistrict() != null) ? address.getStateDistrict() : address.getCounty();
                    String state = address.getState();
                    String postcode = address.getPostcode();
                    String country = address.getCountry();
                    String countryCode = address.getCountryCode();

                    result.add(road);
                    result.add(village);
                    result.add(stateDistrict);
                    result.add(state);
                    result.add(postcode);
                    result.add(countryCode);
                    result.add(country);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
