package com.example.reciclaapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.Objects;

/**
 * Actividad que permite al usuario ubicar su posición en el mapa.
 */
public class SelfLocationStreetMapActivity extends AppCompatActivity {

    MapView MapOS = null;
    GeoPoint StartPoint;
    Marker Mark = null;
    Double Latitud = 0.0;
    Double Longitud = 0.0;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;
    final Context context = this;
    InfoWindow customInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_location_street_map);
        // Find the Toolbar by its ID and set the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for the app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Enable the back button (up navigation)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        showPopupMapExplanation();

        try {

            // Initialize current location service
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            //Init Map
            Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
            InitializeMap();

            //Check permission
            checkLocationPermission();

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception in Logcat
            String errorMessage = e.getMessage(); // Get the error message
            runOnUiThread(() -> Toast.makeText(this, "An error occurred: " + errorMessage, Toast.LENGTH_LONG).show());
        }

    }
    // Inicializa el mapa y configura la interfaz.
    private void InitializeMap() {
        MapOS = findViewById(R.id.osmap);
        MapOS.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        MapOS.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        MapOS.setMultiTouchControls(true);
        MapOS.setHorizontalMapRepetitionEnabled(true);
        MapOS.setVerticalMapRepetitionEnabled(false);
        MapOS.setScrollableAreaLimitLatitude(MapView.getTileSystem().getMaxLatitude(), MapView.getTileSystem().getMinLatitude(), 0);
        MapOS.getController().setZoom(20.0);
        MapOS.setMinZoomLevel(5.0);
        MapOS.getController().setCenter(StartPoint);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    // Verifica y solicita permisos de ubicación.
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Se necesita permiso de ubicación")
                        .setMessage("Esta aplicación necesita el permiso de ubicación, por favor acepta para utilizar la funcionalidad de ubicación")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(SelfLocationStreetMapActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            requestLocation();
        }
    }
    // Maneja la respuesta de la solicitud de permisos de ubicación.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();

            } else {
                Toast.makeText(this, "Este permiso es necesario", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }
    // Obtiene la ubicación actual del usuario.
    @SuppressLint("MissingPermission")
    private void requestLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        mLastLocation = location;

                        if (customInfoWindow == null) {
                            customInfoWindow = new InfoWindow(R.layout.custom_window_self_ubication, MapOS) {
                                @Override
                                public void onOpen(Object item) {

                                }

                                @Override
                                public void onClose() {

                                }
                            };
                        }

                        if (StartPoint == null) {
                            StartPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                            Latitud = location.getLatitude();
                            Longitud = location.getLongitude();
                        } else {
                            StartPoint.setLatitude(location.getLatitude());
                            StartPoint.setLongitude(location.getLongitude());
                        }

                        if (Mark == null) {
                            Mark = new Marker(MapOS);
                            Mark.setPosition(StartPoint);
                            Mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                            Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.icon_pin_map));
                            Mark.setInfoWindow(customInfoWindow);
                            Mark.setDraggable(true);
                            MapOS.getController().setCenter(StartPoint);

                        } else  {
                            Mark.setPosition(StartPoint);
                            Mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                        }

                        Mark.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDrag(Marker marker) {

                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {
                                MarkOnClick();
                            }

                            @Override
                            public void onMarkerDragStart(Marker marker) {

                            }
                        });

                        Mark.setOnMarkerClickListener((marker, mapView) -> {
                            if (marker.isInfoWindowOpen()) {
                                marker.closeInfoWindow();
                            } else {
                                marker.showInfoWindow();
                            }
                            return true;
                        });

                        MapOS.getOverlays().add(Mark);
                    }
                });
    }

    // Acciones al hacer clic en el marcador.
    private void MarkOnClick() {
        StartPoint = Mark.getPosition();
        Latitud = StartPoint.getLatitude();
        Longitud = StartPoint.getLongitude();
    }

    // Finaliza la actividad y muestra la ubicación en otra actividad.
    public void onFinishGpsLocation(View view) {
        Intent intent = new Intent(this, MaterialesActivity.class);
        // Se pasa la información de la actividad previa
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        // esta info es de las coordenadas de la recolección
        intent.putExtra("latitud", ""+Latitud);
        intent.putExtra("longitud", ""+Longitud);
        //Toast.makeText(context, "coordenadas: " + Latitud + ", " + Longitud, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    // Muestra el cuadro de diálogo de explicación del mapa.
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
        final android.app.AlertDialog alertDialog = builder.create();

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

    // Muestra el cuadro de diálogo de explicación del mapa.
    public void onHelpMap(View view) {
        showPopupMapExplanation();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}