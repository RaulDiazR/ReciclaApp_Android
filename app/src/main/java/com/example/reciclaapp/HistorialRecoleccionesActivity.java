package com.example.reciclaapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reciclaapp.models.McqMaterial;
import com.example.reciclaapp.models.McqRecoleccion;
import com.example.reciclaapp.models.McqRecolector;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
/*
   Actividad HistorialRecoleccionesActivity: Muestra el historial de recolecciones del usuario.
   - Utiliza un RecyclerView para mostrar la lista de recolecciones pasadas.
   - Incluye lógica para actualizar el estado de las recolecciones según el tiempo transcurrido.
   - Permite cancelar recolecciones y muestra confirmaciones al usuario.

*/
public class HistorialRecoleccionesActivity extends AppCompatActivity implements HistorialItemClickListener {

    // 1- AdapterView
    RecyclerView recyclerView;
    // 2- DataSource
    List<HistorialItem> itemList;
    // 3- Adapter
    HistorialAdapter historialAdapter;

    // estados de recolecciones
    String iniciada = "Iniciada";
    String enProceso = "En Proceso";
    String completada = "Completada";
    String cancelada = "Cancelada";

    String userId;

    FirebaseFirestore firestore;

    // Se define un handler para agendar actividades cada cierto tiempo
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_recolecciones);

        // Encuentra la barra de herramientas por su ID y configura la barra de herramientas como la barra de aplicaciones
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Eliminar el título predeterminado de la barra de aplicaciones
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        firestore = FirebaseFirestore.getInstance();

        initializeRecyclerView();
        retrieveAndCreateHistorialItems();

    }

    // Inicializa el RecyclerView y sus componentes asociados
    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewHistorial);
        itemList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        historialAdapter = new HistorialAdapter(itemList);
        recyclerView.setAdapter(historialAdapter);


        historialAdapter.setClickListener(this);
    }

    // Recupera datos de Firebase y crea elementos del historial para mostrar en el RecyclerView
    @SuppressLint("NotifyDataSetChanged")
    private void retrieveAndCreateHistorialItems() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        userId = "user_id_2";
        CollectionReference recoleccionesCollection = firestore.collection("recolecciones");

        recoleccionesCollection.whereEqualTo("idUsuarioCliente", userId).orderBy("timeStamp", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(50).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.d("firestorageErrorGet", "Error: " + e);
                return;
            }

            itemList.clear();

            if (queryDocumentSnapshots != null) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    McqRecoleccion recoleccion = snapshot.toObject(McqRecoleccion.class);

                    if (recoleccion.getMateriales() == null) {
                        recoleccion.setMateriales(new HashMap<>());
                    }

                    String materialesQuantityText = (recoleccion.getMateriales().size() != 1) ? " materiales" : " material";
                    String date = recoleccion.getFechaRecoleccion();
                    String time = recoleccion.getHoraRecoleccionFinal();
                    String materialsInfo = "" + recoleccion.getMateriales().size() + materialesQuantityText;
                    String estado = recoleccion.getEstado();
                    int color = getColorForEstado(estado);
                    boolean isRated = recoleccion.getCalificado();
                    Drawable squareDrawable = getDrawableForEstado(estado);
                    String id = snapshot.getId(); // Use the document ID as the unique identifier
                    Long timeStamp = recoleccion.getTimeStamp();
                    boolean enPersona = recoleccion.getEnPersona();
                    boolean recolectada = recoleccion.getRecolectada();


                    Map<String, Object> recolectorData = recoleccion.getRecolector();


                    String recolectorNombre = (String) recolectorData.get("nombre");
                    String recolectorApellidos = (String) recolectorData.get("apellidos");
                    String recolectorTelefono = (String) recolectorData.get("telefono");
                    String recolectorFoto = (String) recolectorData.get("fotoUrl");
                    String recolectorId = (String) recolectorData.get("id");

                    // Se checa si la recolección tiene un recolector asignado
                    if (recolectorId != null && estado.equals(this.iniciada)){
                        // en caso de ser cierto, se cambia el estado de la aplicación
                        if(!recolectorId.equals("")){
                            estado = this.enProceso;
                            color = getColorForEstado(estado);
                            squareDrawable = getDrawableForEstado(estado);
                            updateRecoleccionEstado(id, estado);
                        }
                    } else if (estado.equals(this.enProceso)){
                        // se cambia la recolección a Completada en caso de estar En Proceso y haber sido recolectada por el recolector
                        if(recolectada){
                            estado = this.completada;
                            color = getColorForEstado(estado);
                            squareDrawable = getDrawableForEstado(estado);
                            updateRecoleccionEstado(id, estado);
                        }
                    }

                    Long recolectorCantidadResenas = (Long) recolectorData.get("cantidad_reseñas");
                    Long recolectorSumaResenas = (Long) recolectorData.get("suma_reseñas");

                    int cantidadResenas = recolectorCantidadResenas != null ? recolectorCantidadResenas.intValue() : 0;
                    int sumaResenas = recolectorSumaResenas != null ? recolectorSumaResenas.intValue() : 0;

                    McqRecolector recolector = new McqRecolector(recolectorNombre, recolectorApellidos, recolectorTelefono, recolectorFoto, cantidadResenas, sumaResenas, recolectorId);

                    Map<String, Map<String, Object>> materialesMap = recoleccion.getMateriales();

                    List<McqMaterial> materialesList = new ArrayList<>();

                    for (String materialId : materialesMap.keySet()) {
                        Map<String, Object> materialData = materialesMap.get(materialId);
                        if (materialData != null) {
                            McqMaterial material = new McqMaterial();
                            material.setNombre((String) materialData.get("nombre"));
                            material.setUnidad((String) materialData.get("unidad"));

                            // Se revisa que cantidadMaterial no sea nulo
                            Long cantidadMaterialLong = (Long) materialData.get("cantidad");
                            int cantidadMaterialInt = cantidadMaterialLong != null ? cantidadMaterialLong.intValue() : 0;
                            material.setCantidad(cantidadMaterialInt);

                            material.setFotoUrl((String) materialData.get("fotoUrl"));
                            materialesList.add(material);
                        }
                    }

                    HistorialItem newItem = new HistorialItem(squareDrawable, date, time, materialsInfo, estado, color, isRated, id, recolector, timeStamp, materialesList, enPersona);

                    itemList.add(newItem);
                }
            }
            historialAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        });
    }

    // Muestra un cuadro de diálogo de confirmación antes de cancelar una orden
    public void showCorrectPopup(String estado, int itemPos) {
        HistorialItem curItem = itemList.get(itemPos);

        if(estado.equals(this.iniciada)) {
            final View backgroundView = new View(this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color Semitransparente

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme);

            View dialogView = getLayoutInflater().inflate(R.layout.historial_popup_iniciada, null);

            builder.setView(dialogView);

            final AlertDialog alertDialog = builder.create();

            Window window = alertDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.CENTER;
                window.setAttributes(params);
            }

            alertDialog.show();

            Button btnCancelarOrden = dialogView.findViewById(R.id.cancelarOrdenButton);
            Button btnContinuar = dialogView.findViewById(R.id.continuarButton);
            Button btnVerDetalles = dialogView.findViewById(R.id.verDetallesButton);

            btnVerDetalles.setOnClickListener(v -> {
                Intent intent = new Intent(this, VerDetallesActivity.class);
                String materialesList = new Gson().toJson(curItem.getMaterialesList());
                intent.putExtra("data",materialesList);
                intent.putExtra("fecha", curItem.getFecha());
                intent.putExtra("horario", curItem.getHorario());
                intent.putExtra("enPersona", curItem.getEnPersona());
                startActivity(intent);
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView); // Remove the background

            });

            btnCancelarOrden.setOnClickListener(v -> {
                alertDialog.dismiss();
                confirmarCancelarOrden(itemPos);
            });

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

            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.addView(backgroundView);
        }

        else if(estado.equals(this.enProceso)) {
            final View backgroundView = new View(this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0));

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme);

            View dialogView = getLayoutInflater().inflate(R.layout.historial_popup_en_proceso, null);

            builder.setView(dialogView);

            final AlertDialog alertDialog = builder.create();

            Window window = alertDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.CENTER;
                window.setAttributes(params);
            }

            alertDialog.show();

            // Se agrega el nombre del recolector
            TextView recolector = dialogView.findViewById(R.id.recolector);
            recolector.setText(curItem.getRecolector().generarNombreCompleto());

            // Se agrega la imagen del recolector
            ImageFilterView recolectorImg = dialogView.findViewById(R.id.recolectorImg);
            String url = curItem.getRecolector().getFotoUrl();
            if (url.equals("")){
                recolectorImg.setImageResource(R.drawable.icon_user_gray);
            }
            else {
                Picasso.get().load(url).placeholder(R.drawable.icon_loading).error(R.drawable.icon_user_gray).into(recolectorImg);
            }

            // Se agrega el teléfono del recolector
            TextView recolectorTelefono = dialogView.findViewById(R.id.recolectorTelefono);
            recolectorTelefono.setText(curItem.getRecolector().getTelefono());

            // se agrega la calificación del recolector
            TextView ratingRecolector = dialogView.findViewById(R.id.calificacionRecolector);
            String calificacion = String.format(Locale.getDefault(), "%.1f", curItem.getRecolector().calcularCalificacion());
            ratingRecolector.setText(calificacion);

            Button btnContinuar = dialogView.findViewById(R.id.continuarButton);
            Button btnVerDetalles = dialogView.findViewById(R.id.verDetallesButton);
            Button btnCancelarOrden = dialogView.findViewById(R.id.cancelarOrdenButton);

            btnCancelarOrden.setOnClickListener(v -> {
                alertDialog.dismiss();
                confirmarCancelarOrden(itemPos);
            });

            btnVerDetalles.setOnClickListener(v -> {
                Intent intent = new Intent(this, VerDetallesActivity.class);
                String materialesList = new Gson().toJson(curItem.getMaterialesList());
                intent.putExtra("data",materialesList);
                intent.putExtra("fecha", curItem.getFecha());
                intent.putExtra("horario", curItem.getHorario());
                intent.putExtra("enPersona", curItem.getEnPersona());
                startActivity(intent);
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView); // Remove the background

            });

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

        else if(estado.equals(this.completada)) {

            final View backgroundView = new View(this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme); // Apply the custom theme

            View dialogView = getLayoutInflater().inflate(R.layout.historial_popup_completada, null);

            builder.setView(dialogView);

            final AlertDialog alertDialog = builder.create();

            Window window = alertDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.CENTER;
                window.setAttributes(params);
            }

            alertDialog.show();

            TextView recolector = dialogView.findViewById(R.id.recolector);
            recolector.setText(curItem.getRecolector().generarNombreCompleto());

            LinearLayout linearLayoutRatingSection = dialogView.findViewById(R.id.linearLayoutRating);
            RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
            Button btnContinuar = dialogView.findViewById(R.id.continuarButton);
            Button btnVerDetalles = dialogView.findViewById(R.id.verDetallesButton);

            btnVerDetalles.setOnClickListener(v -> {
                Intent intent = new Intent(this, VerDetallesActivity.class);
                String materialesList = new Gson().toJson(curItem.getMaterialesList());
                intent.putExtra("data",materialesList);
                intent.putExtra("fecha", curItem.getFecha());
                intent.putExtra("horario", curItem.getHorario());
                intent.putExtra("enPersona", curItem.getEnPersona());
                startActivity(intent);
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView); // Remove the background

            });

            ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
                Resources res = getResources();
                Drawable button_variant_green = ResourcesCompat.getDrawable(res, R.drawable.button_variant_green, null);
                btnContinuar.setBackground(button_variant_green);
                btnContinuar.setEnabled(true);
            });

            btnContinuar.setOnClickListener(v -> {
                if (!curItem.getIsRated()) {
                    Toast.makeText(this,
                            "DB changed!!!",
                            Toast.LENGTH_LONG).show();
                    DocumentReference recolectorRef = firestore.collection("recolectores").document(curItem.getRecolector().getId());

                    // Atomically increment the values.
                    recolectorRef.update("cantidad_reseñas", FieldValue.increment(1));
                    recolectorRef.update("suma_reseñas", FieldValue.increment((int) ratingBar.getRating()));
                    updateRecoleccionCalificadoBool(curItem.getId());
                    curItem.setRated(true);
                }
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView); // Remove the background
            });

            alertDialog.setOnDismissListener(v -> {
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView); // Remove the background
            });

            // Se agrega la imagen del recolector
            ImageFilterView recolectorImg = dialogView.findViewById(R.id.recolectorImg);
            String url = curItem.getRecolector().getFotoUrl();
            if (url.equals("")){
                recolectorImg.setImageResource(R.drawable.icon_user_gray);
            }
            else {
                Picasso.get().load(url).placeholder(R.drawable.icon_loading).error(R.drawable.icon_user_gray).into(recolectorImg);
            }

            if (curItem.getIsRated()) {
                linearLayoutRatingSection.setVisibility(View.GONE);
                Resources res = getResources();
                Drawable button_variant_green = ResourcesCompat.getDrawable(res, R.drawable.button_variant_green, null);
                btnContinuar.setBackground(button_variant_green);
                btnContinuar.setEnabled(true);
            }

            // Add the background view and show the dialog
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.addView(backgroundView);
        }

        else if(estado.equals(this.cancelada)) {

            final View backgroundView = new View(this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0));

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme);

            View dialogView = getLayoutInflater().inflate(R.layout.historial_popup_cancelada, null);


            builder.setView(dialogView);

            final AlertDialog alertDialog = builder.create();

            Window window = alertDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.CENTER;
                window.setAttributes(params);
            }

            alertDialog.show();

            Button btnVerCentros = dialogView.findViewById(R.id.verCentrosButton);
            Button btnContinuar = dialogView.findViewById(R.id.continuarButton);
            Button btnVerDetalles = dialogView.findViewById(R.id.verDetallesButton);

            btnVerDetalles.setOnClickListener(v -> {
                Intent intent = new Intent(this, VerDetallesActivity.class);
                String materialesList = new Gson().toJson(curItem.getMaterialesList());
                intent.putExtra("data",materialesList);
                intent.putExtra("fecha", curItem.getFecha());
                intent.putExtra("horario", curItem.getHorario());
                intent.putExtra("enPersona", curItem.getEnPersona());
                startActivity(intent);
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView); // Remove the background

            });

            btnVerCentros.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });

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

            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.addView(backgroundView);
        }

    }

    // Se define un TASK que se repite cada cierto tiempo
    private final Runnable updateStatusTask = new Runnable() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {

            TimeZone cstTimeZone = TimeZone.getTimeZone("America/Chicago"); // CST time zone
            Calendar currentCalendar = Calendar.getInstance(cstTimeZone);
            Date currentDate = currentCalendar.getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy HH:mm");

            for (HistorialItem item : itemList) {
                String fechaRecoleccionString = item.getFecha();
                String horaRecoleccionFinalString = item.getHorario();
                Date horaRecoleccionFinal;
                try {
                    horaRecoleccionFinal = dateFormat.parse(fechaRecoleccionString + " " + horaRecoleccionFinalString);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (currentDate.after(horaRecoleccionFinal) && item.getEstado().equals("Iniciada") && !item.getEstado().equals("Cancelada")) {

                    String estado = "Cancelada";
                    item.setEstado(estado);
                    item.setEstadoColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
                    item.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_square_red, null));
                    updateRecoleccionEstado(item.getId(),estado);
                }
            }

            historialAdapter.notifyDataSetChanged();

            int minutes = 1;
            handler.postDelayed(this, minutes * 60 * 1000); // 1 minuto en milisegundos
        }
    };

    // Comienza la tarea de actualización periódica cuando la actividad se crea o se reanuda
    @Override
    protected void onResume() {
        super.onResume();
        // Start the initial task and schedule it to run periodically
        handler.post(updateStatusTask);
    }

    // Detiene la tarea de actualización periódica cuando la actividad se pausa o se destruye
    @Override
    protected void onPause() {
        super.onPause();
        // Remove any pending callbacks to stop the task
        handler.removeCallbacks(updateStatusTask);
    }

    public void confirmarCancelarOrden(int itemPos) {
        // Create a view for the semitransparent background
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme); // Apply the custom theme

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.historial_confirmar_cancelar_orden, null);

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
        Button btnConfirmar = dialogView.findViewById(R.id.ConfirmarButton);
        Button btnCancelar = dialogView.findViewById(R.id.CancelarButton);

        // Logic for Confirm
        btnConfirmar.setOnClickListener(v -> {
            // Logic for Confirm
            if (itemPos != RecyclerView.NO_POSITION) {
                String itemId = itemList.get(itemPos).getId();
                updateRecoleccionEstado(itemId, this.cancelada);
            }
            alertDialog.dismiss();
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.removeView(backgroundView); // Remove the background
        });

        btnCancelar.setOnClickListener(v -> {
            // Logic for Cancel
            alertDialog.dismiss();
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.removeView(backgroundView); // Remove the background
        });

        alertDialog.setOnDismissListener(v -> {
            // Logic for Cancel
            alertDialog.dismiss();
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.removeView(backgroundView); // Remove the background
        });

        // Add the background view and show the dialog
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);
    }

    // Lógica para manejar clicks en elementos del RecyclerView
    @Override
    public void onClick(View v, int pos) {

        HistorialItem clickedItem = itemList.get(pos);
        String estado = clickedItem.getEstado();

        showCorrectPopup(estado, pos);
    }

    // Actualiza el estado de una recolección en Firebase
    private void updateRecoleccionEstado(String recoleccionIdToUpdate, String newEstado) {
        // Initialize a Firestore DocumentReference for the recolección document
        firestore.collection("recolecciones").document(recoleccionIdToUpdate).update("estado", newEstado).addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                Toast.makeText(HistorialRecoleccionesActivity.this, "Lo sentimos, ocurrió un error, inténtelo de nuevo más tarde", Toast.LENGTH_LONG).show();
            }
        });

    }

    // Actualiza el atributo "calificado" de una recolección en Firebase
    private void updateRecoleccionCalificadoBool(String recoleccionIdToUpdate) {
        // Initialize a Firestore DocumentReference for the recolección document
        firestore.collection("recolecciones").document(recoleccionIdToUpdate).update("calificado", true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                Toast.makeText(HistorialRecoleccionesActivity.this, "Lo sentimos, ocurrió un error, inténtelo de nuevo más tarde", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Implementa una función para asignar color según el estado
    private int getColorForEstado(String estado) {
        Resources res = getResources();
        // Implement your logic to map estado to a color here
        if (estado.equals(this.iniciada)) {
            return ResourcesCompat.getColor(res, R.color.golden, null);
        }
        else if (estado.equals(this.enProceso)) {
            return ResourcesCompat.getColor(res, R.color.blue, null);
        }
        else if (estado.equals(this.completada)) {
            return ResourcesCompat.getColor(res, R.color.green, null);
        }
        else if (estado.equals(this.cancelada)) {
            return ResourcesCompat.getColor(res, R.color.red, null);
        }
        else {
            return ResourcesCompat.getColor(res, R.color.green, null);
        }
    }

    // Implementa una función para asignar Drawable según el estado
    private Drawable getDrawableForEstado(String estado) {
        Resources res = getResources();
        // Implement your logic to map estado to a color here
        if (estado.equals(this.iniciada)) {
            return ResourcesCompat.getDrawable(res, R.drawable.shape_square_gold, null);
        }
        else if (estado.equals(this.enProceso)) {
            return ResourcesCompat.getDrawable(res, R.drawable.shape_square_blue, null);
        }
        else if (estado.equals(this.completada)) {
            return ResourcesCompat.getDrawable(res, R.drawable.shape_square_green, null);
        }
        else if (estado.equals(this.cancelada)) {
            return ResourcesCompat.getDrawable(res, R.drawable.shape_square_red, null);
        }
        else {
            return ResourcesCompat.getDrawable(res, R.drawable.shape_square_green, null);
        }
    }

    // Agrega una nueva orden
    public void addOrder (View v) {
        Intent intent = new Intent(this, OrdenHorarioActivity.class);
        startActivity(intent);
    }
}