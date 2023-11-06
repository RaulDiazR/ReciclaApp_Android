package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;
import java.util.Locale;

public class SelfLocationStreetMapActivity extends AppCompatActivity {

    MapView Map = null;
    GeoPoint StartPoint;
    Marker Mark = null;
    Double Latitud = 0.0;
    Double Longitud = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_location_street_map);
        showPopupMapExplanation();

        // Get Direction Of User
        Intent intent = getIntent();
        String postalCode = "72810";
        String state = "Puebla";
        String city = "Xicontenco";
        String country = "Mexico";
        String street = "Sena";
        String county = "";

        final String[] finalPostalCode = {postalCode};
        final String[] finalState = {state};
        final String[] finalCity = {city};
        final String[] finalCountry = {country};
        final String[] finalStreet = {street};


        // Create the string to send the request to the Nominatim API
        String DirectionCoordinates = "https://nominatim.openstreetmap.org/search?" + "street=" + street.replace(" ", "+") + "&city=" + city.replace(" ", "+") + "&postalcode=" + postalCode + "&country=" + country + "&state=" + state + "&format=jsonv2";
        Log.d("TAG", "URL " + DirectionCoordinates);
        GetCoordinates(DirectionCoordinates);

        //Create Map and Put the Marker
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Map = (MapView) findViewById(R.id.osmap);
        Map.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        Map.setBuiltInZoomControls(true);
        Map.setMultiTouchControls(true);
        Map.getController().setZoom(20.0);
        //StartPoint = new GeoPoint(19.0653127, -98.2021354);

        StartPoint = new GeoPoint(Latitud, Longitud);
        Map.getController().setCenter(StartPoint);

        Mark = new Marker(Map);
        Mark.setPosition(StartPoint);
        Mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        Mark.setTitle("Mi ubicacion");
        Mark.setDraggable(true);

        // Add mark to MapLayer OSM
        Map.getOverlays().add(Mark);




        // Agrega un OnMarkerDragListener al marcador
        Mark.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {
                // Evento mientras el usuario está arrastrando el marcador
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onMarkerDragEnd(Marker marker) {

                Log.d("TAG", "Drag End Activated" );
                new ReverseGeocodingTask() {
                    @Override
                    protected void onPostExecute(List<String> response) {
                        if (!response.isEmpty()) {
                            // Process the response here
                            finalPostalCode[0] = response.get(4);
                            finalState[0] = response.get(3);
                            finalCity[0] = response.get(2);
                            finalCountry[0] = response.get(6);
                            finalStreet[0] = response.get(0);
                            Log.d("TAG", "Marker: Entre" );
                            Log.d("TAG", "finalPostalCode: " + finalPostalCode[0]);
                            Log.d("TAG", "finalState: " + finalState[0]);
                            Log.d("TAG", "finalCity: " + finalCity[0]);
                            Log.d("TAG", "finalCountry: " + finalCountry[0]);
                            Log.d("TAG", "finalStreet: " + finalStreet[0]);

                            StartPoint = marker.getPosition();
                            Map.getController().setCenter(StartPoint);
                        }
                    }
                }.execute(marker.getPosition());

            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                // Evento cuando el usuario comienza a arrastrar el marcador
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    public void GetCoordinates(String URL) {
        final Context context = this;
        new GeocodingTask() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(List<Double> response) {
                if (!response.isEmpty()) {
                    // Process the response here
                    Latitud = response.get(0);
                    Longitud = response.get(1);
                    StartPoint.setLatitude(Latitud);
                    StartPoint.setLongitude(Longitud);
                    Map.getController().setCenter(StartPoint);
                    Mark.setPosition(StartPoint);
                    Log.d("TAG", "Lista Coordenadas: " + response);
                    Log.d("TAG", "Latitud: " + Latitud);
                    Log.d("TAG", "Longitud: " + Longitud);
                } else {
                    Log.e("TAG", "Error al obtener la ubicación");
                    Toast.makeText(context, "Error al obtener la ubicación", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(URL);
    }

    public void onFinishGpsLocation(View view) {
        Intent intent = new Intent(this, MaterialesActivity.class);
        // Se pasa la información de la actividad previa
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void showPopupMapExplanation() {

        // Create a view for the semitransparent background
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme); // Apply the custom theme

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.mapa_explicacion_popup, null);

        // Configure the dialog
        builder.setView(dialogView);

        // Customize the dialog
        final AlertDialog alertDialog = builder.create();

        // Configure a semitransparent background
        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }

        alertDialog.show();

        // Configure button actions
        Button btnContinuar = dialogView.findViewById(R.id.continuarBtn);

        btnContinuar.setOnClickListener(v -> {
            alertDialog.dismiss();
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.removeView(backgroundView); // Remove the background
        });

        alertDialog.setOnDismissListener(v -> {
            alertDialog.dismiss();
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.removeView(backgroundView); // Remove the background
        });

        // Add the background view and show the dialog
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);
    }

    public void onHelpMap(View view) {
        showPopupMapExplanation();
    }
}