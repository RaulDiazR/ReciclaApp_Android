package com.example.reciclaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class Center extends AppCompatActivity {

    private String nombre;
    private String direccion;
    private double latitud;
    private double longitud;
    private String num_telefonico;
    private String hora_apertura;
    private String hora_cierre;
    private String imagen;
    private GeoPoint StartPoint;
    private Marker Mark;
    private InfoWindow customInfoWindow;
    private MapView Map;
    private Context context;
    private String categoria;
    private Activity activity;
    private boolean isFavorite;

    public Center(String nombre, String direccion, Double latitud, Double longitud, String num_telefonico, String hora_apertura, String hora_cierre, String imagen, MapView map, Context context1, String category, Activity activity) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.num_telefonico = num_telefonico;
        this.hora_apertura = hora_apertura;
        this.hora_cierre = hora_cierre;
        this.imagen = imagen;
        this.Map = map;
        this.context = context1;
        this.categoria = category;
        this.activity = activity;
        this.isFavorite = false;

        StartPoint = new GeoPoint(this.latitud, this.longitud);
        this.Mark = new Marker(this.Map);
        this.Mark.setPosition(StartPoint);
        this.Mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        this.Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.icon_pin_self));

        createInfoWindow();
    }

    private void setFavorite() {
        if (this.isFavorite) {
            this.isFavorite = false;
        } else {
            this.isFavorite = true;
        }
        changeFavoriteBD();
    }

    private void changeFavoriteBD() {
        //Modify in BD with new value
    }

    private void createInfoWindow() {

        if (this.customInfoWindow == null) {
            this.customInfoWindow = new InfoWindow(R.layout.custom_info_window_maker, this.Map) {
                @Override
                public void onOpen(Object item) {

                    TextView centerName = mView.findViewById(R.id.nameCenter);
                    TextView contactInfo = mView.findViewById(R.id.contactCenter);
                    TextView addToFavorites = mView.findViewById(R.id.addToFavorites);
                    ImageView centerImage = mView.findViewById(R.id.iconImageView);
                    LinearLayout moreCenterLayout = mView.findViewById(R.id.moreCenterLayout);

                    ImageButton phoneContact = mView.findViewById(R.id.phone_contact);
                    ToggleButton favoriteCenter = mView.findViewById(R.id.add_button);

                    centerName.setText(nombre);
                    contactInfo.setText(num_telefonico);
                    if (isFavorite) {
                        addToFavorites.setText("Favorito");
                    } else {
                        addToFavorites.setText("Agregar a favoritos");
                    }
                    Picasso.get().load(imagen).into(centerImage);

                    phoneContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            phoneCall();
                        }
                    });
                    favoriteCenter.setChecked(isFavorite);
                    favoriteCenter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setFavorite();

                            if (isFavorite) {
                                addToFavorites.setText("Favorito");
                            } else {
                                addToFavorites.setText("Agregar a favoritos");
                            }
                        }
                    });
                    moreCenterLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openMoreInfoCenter();
                        }
                    });

                }
                @Override
                public void onClose() {

                }
            };

            this.Mark.setInfoWindow(this.customInfoWindow);

            this.Mark.setOnMarkerClickListener((marker, mapView) -> {
                if (marker.isInfoWindowOpen()) {
                    marker.closeInfoWindow();
                } else {
                    marker.showInfoWindow();
                }

                return true;
            });
        }
    }

    private void phoneCall() {
        try {

            String numeroTelefono = "tel:+52" + this.num_telefonico;
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(numeroTelefono));
            this.activity.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception in Logcat
            String errorMessage = e.getMessage(); // Get the error message
            runOnUiThread(() -> {
                Toast.makeText(context, "An error occurred: " + errorMessage, Toast.LENGTH_LONG).show();
            });
        }
    }

    public void openMoreInfoCenter(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.center_info,null);
        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        ImageView closeButton = mView.findViewById(R.id.closeButton);
        TextView centerName = mView.findViewById(R.id.titleTextView);
        TextView centerDirectiion = mView.findViewById(R.id.descriptionTextView);
        TextView centerHours = mView.findViewById(R.id.horarioTextView);
        ImageView centerImage = mView.findViewById(R.id.imageViewCenter);

        centerName.setText(getNombre());
        centerDirectiion.setText(getDireccion());
        centerHours.setText(getHora_apertura() + " - " + getHora_cierre());
        Picasso.get().load(getImagen()).into(centerImage);

        closeButton.setOnClickListener(view1 -> {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNum_telefonico() {
        return num_telefonico;
    }

    public void setNum_telefonico(String num_telefonico) {
        this.num_telefonico = num_telefonico;
    }

    public String getHora_apertura() {
        return hora_apertura;
    }

    public void setHora_apertura(String hora_apertura) {
        this.hora_apertura = hora_apertura;
    }

    public String getHora_cierre() {
        return hora_cierre;
    }

    public void setHora_cierre(String hora_cierre) {
        this.hora_cierre = hora_cierre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Marker getMark() {
        return Mark;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
