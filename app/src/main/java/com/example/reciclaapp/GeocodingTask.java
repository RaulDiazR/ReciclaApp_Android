package com.example.reciclaapp;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GeocodingTask extends AsyncTask<String, Void, List<Double>> {

    List<Double> result = new ArrayList<>();

    @Override
    protected List<Double> doInBackground(String... strings) {

        if (strings.length == 0) {
            return result;
        }
        try {
            URL url = new URL(strings[0]);
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

                result = processGeocodingResponse(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    private List<Double> processGeocodingResponse(String response) {

        List<Double> result = new ArrayList<>();

        try {
            Gson gson = new Gson();
            List<Cordinates> cordinatesList = gson.fromJson(response, new TypeToken<List<Cordinates>>() {}.getType());
            if (!cordinatesList.isEmpty()) {
                Cordinates coordinates = cordinatesList.get(0);
                String Lat = coordinates.getLatitude();
                String Lon = coordinates.getLongitude();
                result.add(Double.parseDouble(Lat));
                result.add(Double.parseDouble(Lon));}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
