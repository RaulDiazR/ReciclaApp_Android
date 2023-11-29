package com.greencarson.reciclaapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class SelfLocationStreetMap extends AppCompatActivity {

    MapView MapOS = null;
    GeoPoint StartPoint;
    Marker Mark = null;
    Double Latitud = 0.0;
    Double Longitud = 0.0;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;
    final Context context = this;
    InfoWindow customInfoWindow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_location_street_map);


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
            runOnUiThread(() -> {
                Toast.makeText(this, "An error occurred: " + errorMessage, Toast.LENGTH_LONG).show();
            });
        }

    }

    private void MarkOnClick() {
        StartPoint = Mark.getPosition();
        Latitud = StartPoint.getLatitude();
        Longitud = StartPoint.getLongitude();
    }

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
                            ActivityCompat.requestPermissions(SelfLocationStreetMap.this,
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    requestLocation();

                } else {
                    Toast.makeText(this, "Permiso Denegado", Toast.LENGTH_LONG).show();

                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void requestLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
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
                            } else {
                                StartPoint.setLatitude(location.getLatitude());
                                StartPoint.setLongitude(location.getLongitude());
                            }

                            if (Mark == null) {
                                Mark = new Marker(MapOS);
                                Mark.setPosition(StartPoint);
                                Mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.icon_pin_self));
                                Mark.setInfoWindow(customInfoWindow);
                                Mark.setDraggable(true);
                                MapOS.getController().setCenter(StartPoint);

                            } else {
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
                            MapOS.getOverlays().add(Mark);
                        }
                    }
                });
    }
}