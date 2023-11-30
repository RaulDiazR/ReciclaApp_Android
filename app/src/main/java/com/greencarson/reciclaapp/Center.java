package com.greencarson.reciclaapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.List;

public class Center extends AppCompatActivity {

    private String nombre;
    private String direccion;
    private double latitud;
    private double longitud;
    private String num_telefonico;
    private String hora_apertura;
    private String hora_cierre;
    private String imagen;
    private GeoPoint StartPoint;         //CLCoordinate
    private Marker Mark;                 //MKMarkerAnnotation
    private InfoWindow customInfoWindow; // MKMarkerAnnotationView
    private MapView Map;
    private Context context;
    private String categoria;
    private Activity activity;
    private boolean isFavorite;
    private ArrayList<MaterialModel> materiales;
    private List<String> dias;
    private RecyclerView materialesRV;
    private MaterialRVAdapter materialRVAdapter;
    private StreetMapActivity parent;

    public Center(String nombre, String direccion, Double latitud, Double longitud, String num_telefonico, String hora_apertura, String hora_cierre, String imagen, MapView map, Context context1, String category, Activity activity, ArrayList<MaterialModel> mate, List<String> days, StreetMapActivity par) {
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
        this.materiales = mate;
        this.dias = days;
        this.parent = par;

        StartPoint = new GeoPoint(this.latitud, this.longitud);
        this.Mark = new Marker(this.Map);
        this.Mark.setPosition(StartPoint);
        this.Mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        switch (this.categoria) {
            case "Acopio":
                this.Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.acopio));
                break;
            case "Acciones municipales":
                this.Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.accionesmunicipales));
                break;
            case "Compra-venta":
                this.Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.reciclaje));
                break;
            case "Donaciones":
                this.Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.donaciones));
                break;
            case "Estación de carga":
                this.Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.centrocarga));
                break;
            case "Membresía":
                this.Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.membresia));
                break;
            case "Punto verde":
                this.Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.puntoverde));
                break;
            case "Reparación":
                this.Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.reparaciones));
                break;
            default:
                this.Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.icon_pin_map));
        }

        createInfoWindow();
    }

    public void setFavorite(Boolean val) {
        this.isFavorite = val;
    }

    private void createInfoWindow() {

        if (this.customInfoWindow == null) {
            this.customInfoWindow = new InfoWindow(R.layout.custom_info_window_maker, this.Map) {
                @Override
                public void onOpen(Object item) {

                    TextView centerName = mView.findViewById(R.id.nameCenter);
                    TextView contactInfo = mView.findViewById(R.id.contactCenter);
                    ImageView centerImage = mView.findViewById(R.id.iconImageView);
                    LinearLayout moreCenterLayout = mView.findViewById(R.id.moreCenterLayout);
                    LinearLayout contactCenterLayout = mView.findViewById(R.id.contactCenterLayout);

                    ImageButton phoneContact = mView.findViewById(R.id.phone_contact);

                    centerName.setText(nombre);

                    if (num_telefonico != null && !TextUtils.isEmpty(num_telefonico) && TextUtils.isDigitsOnly(num_telefonico)) {
                        contactInfo.setText(num_telefonico);
                    } else {
                        contactInfo.setText("Sin Numero");
                    }

                    centerImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    if (!imagen.isEmpty()) {
                        Picasso.get().load(imagen).into(centerImage);
                    }

                    contactCenterLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            phoneCall();
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

    public ArrayList<MaterialModel> getMateriales() {
        return materiales;
    }

    public void setMateriales(ArrayList<MaterialModel> materiales) {
        this.materiales = materiales;
    }

    public List<String> getDias() {
        return dias;
    }

    public void setDias(List<String> dias) {
        this.dias = dias;
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

    public void redirectToGoogleMaps() {

        try {

            double lat = this.latitud;
            double lon = this.longitud;
            String uri = String.format("https://www.google.com/maps?q=%f,%f", lat, lon);
            System.out.println(Uri.parse(uri));
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void redirectToGoogleWaze() {

            try {
                double lat = this.latitud;
                double lon = this.longitud;
                @SuppressLint("DefaultLocale")
                String uri = String.format("https://waze.com/ul?ll=%f,%f&navigate=yes", lat, lon);
                String insta = "https://www.instagram.com/photocinematica/";
                System.out.println(Uri.parse(insta));
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(insta)));
            } catch (Exception e) {
                e.printStackTrace();
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
        TextView centerDays = mView.findViewById(R.id.diasTextView);
        ImageView centerImage = mView.findViewById(R.id.imageViewCenter);

        Button maps = mView.findViewById(R.id.imagenIndicacion1);
        Button waze = mView.findViewById(R.id.imagenIndicacion2);

        materialesRV = mView.findViewById(R.id.materialesRecyclerView);


        centerName.setText(getNombre());
        centerDirectiion.setText(getDireccion());
        centerHours.setText(getHora_apertura() + " - " + getHora_cierre());
        centerDays.setText(String.join("-", this.dias));
        centerImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (!imagen.isEmpty()) {
            Picasso.get().load(getImagen()).into(centerImage);
        }


        materialesRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        materialesRV.setLayoutManager(layoutManager);

        materialRVAdapter = new MaterialRVAdapter(materiales, context);

        materialesRV.setAdapter(materialRVAdapter);

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToGoogleMaps();
            }
        });

        waze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToGoogleWaze();
            }
        });

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
