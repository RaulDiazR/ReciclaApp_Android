package com.example.reciclaapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reciclaapp.models.McqRecoleccion;
import com.example.reciclaapp.models.McqRecolector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_recolecciones);

        // Find the Toolbar by its ID and et the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        firestore = FirebaseFirestore.getInstance();

        initializeRecyclerView();
        retrieveAndCreateHistorialItems();

    }

    private void initializeRecyclerView() {
        // Create and set the linear layout manager so the RecyclerView works properly.
        recyclerView = findViewById(R.id.recyclerViewHistorial);
        itemList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize your RecyclerView with the itemList that now contains data from Firebase.
        historialAdapter = new HistorialAdapter(itemList);
        recyclerView.setAdapter(historialAdapter);

        historialAdapter.setClickListener(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void retrieveAndCreateHistorialItems() {
        ProgressBar progressBar = findViewById(R.id.progressBar); // Replace with the ID of your ProgressBar in XML
        progressBar.setVisibility(View.VISIBLE);

        String idUsuarioClienteToFilter = "user_id_1"; // Replace with the actual user ID you want to filter by

        CollectionReference recoleccionesCollection = firestore.collection("recolecciones");

        recoleccionesCollection.whereEqualTo("idUsuarioCliente", idUsuarioClienteToFilter).orderBy("timeStamp", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("firestorageErrorGet", "Error: " + e);
                    return;
                }

                // Inside your addSnapshotListener callback
                if (queryDocumentSnapshots != null) {
                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                // Handle the addition of a new document
                                // Create a HistorialItem for the new document and add it to your UI
                                HistorialItem newItemAdded = createHistorialItemFromRecoleccion(dc.getDocument());
                                itemList.add(newItemAdded);
                                historialAdapter.notifyDataSetChanged();
                                break;
                            case MODIFIED:
                                // Handle the modification of an existing document
                                // Update the corresponding HistorialItem in your UI
                                updateHistorialItemFromRecoleccion(dc.getDocument());
                                break;
                            case REMOVED:
                                // Handle the removal of a document
                                String removedItemId = dc.getDocument().getId();
                                int removedItemIndex = findItemIndexById(removedItemId);
                                if (removedItemIndex != -1) {
                                    itemList.remove(removedItemIndex);
                                    historialAdapter.notifyDataSetChanged();
                                }
                                break;
                        }
                    }
                }

                itemList.clear();

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        McqRecoleccion recoleccion = snapshot.toObject(McqRecoleccion.class);

                        String materialesQuantityText = (recoleccion.getMateriales().size() != 1) ? " materiales" : " material";
                        String date = recoleccion.getFechaRecoleccion();
                        String time = recoleccion.getHoraRecoleccionFinal();
                        String materialsInfo = "" + recoleccion.getMateriales().size() + materialesQuantityText;
                        String estado = recoleccion.getEstado();
                        int color = getColorForEstado(estado);
                        boolean isRated = recoleccion.getCalificado();
                        Drawable squareDrawable = getDrawableForEstado(estado);
                        String id = snapshot.getId(); // Use the document ID as the unique identifier
                        String idRecolector = recoleccion.getIdRecolector();
                        Long timeStamp = recoleccion.getTimeStamp();

                        if (!Objects.equals(idRecolector, "")) {
                            // Directly query the recolectores node for the specific recolector by id
                            DocumentReference recolectorRef = firestore.collection("recolectores").document(idRecolector);

                            recolectorRef.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        // The document exists; you can safely retrieve its data
                                        McqRecolector recolector = document.toObject(McqRecolector.class);
                                        if (recolector != null) {
                                            // Create a new HistorialItem with Recolector data
                                            HistorialItem newItem = new HistorialItem(squareDrawable, date, time, materialsInfo, estado, color, isRated, id, recolector, timeStamp);
                                            itemList.add(newItem);
                                            historialAdapter.notifyDataSetChanged();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    } else {

                                        // Create a new HistorialItem with Recolector data
                                        HistorialItem newItem = new HistorialItem(squareDrawable, date, time, materialsInfo, estado, color, isRated, id, null, timeStamp);

                                        itemList.add(newItem);
                                        historialAdapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } else {
                                    // Handle any errors or exceptions related to retrieving the document
                                    Log.d("firestorageRecolectorError", "Couldn't access fireStorage DB");
                                }
                            });
                        }
                        else {


                            // Create a new HistorialItem with Recolector data
                            HistorialItem newItem = new HistorialItem(squareDrawable, date, time, materialsInfo, estado, color, isRated, id, null, timeStamp);
                            itemList.add(newItem);
                            historialAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }


        });

    }

    private HistorialItem createHistorialItemFromRecoleccion(DocumentSnapshot recoleccionSnapshot) {
        McqRecoleccion recoleccion = recoleccionSnapshot.toObject(McqRecoleccion.class);

        if (recoleccion != null) {
            String materialesQuantityText = (recoleccion.getMateriales().size() != 1) ? " materiales" : " material";
            String date = recoleccion.getFechaRecoleccion();
            String time = recoleccion.getHoraRecoleccionFinal();
            String materialsInfo = "" + recoleccion.getMateriales().size() + materialesQuantityText;
            String estado = recoleccion.getEstado();
            int color = getColorForEstado(estado);
            boolean isRated = recoleccion.getCalificado();
            Drawable squareDrawable = getDrawableForEstado(estado);
            String id = recoleccionSnapshot.getId(); // Use the document ID as the unique identifier
            Long timeStamp = recoleccion.getTimeStamp();

            return new HistorialItem(squareDrawable, date, time, materialsInfo, estado, color, isRated, id, null, timeStamp);
        }
        else {
            return new HistorialItem();
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private void updateHistorialItemFromRecoleccion(DocumentSnapshot recoleccionSnapshot) {
        McqRecoleccion recoleccion = recoleccionSnapshot.toObject(McqRecoleccion.class);

        if (recoleccion != null) {
            String materialesQuantityText = (recoleccion.getMateriales().size() != 1) ? " materiales" : " material";
            String date = recoleccion.getFechaRecoleccion();
            String time = recoleccion.getHoraRecoleccionFinal();
            String materialsInfo = "" + recoleccion.getMateriales().size() + materialesQuantityText;
            String estado = recoleccion.getEstado();
            int color = getColorForEstado(estado);
            boolean isRated = recoleccion.getCalificado();
            Drawable squareDrawable = getDrawableForEstado(estado);
            String id = recoleccionSnapshot.getId(); // Use the document ID as the unique identifier
            String idRecolector = recoleccion.getIdRecolector();
            Long timeStamp = recoleccion.getTimeStamp();

            if (!Objects.equals(idRecolector, "")) {
                // Directly query the recolectores node for the specific recolector by id
                DocumentReference recolectorRef = firestore.collection("recolectores").document(idRecolector);

                recolectorRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // The document exists; you can safely retrieve its data
                            McqRecolector recolector = document.toObject(McqRecolector.class);
                            if (recolector != null) {
                                // Create a new HistorialItem with Recolector data
                                HistorialItem newItem = new HistorialItem(squareDrawable, date, time, materialsInfo, estado, color, isRated, id, recolector, timeStamp);
                                itemList.add(newItem);
                                historialAdapter.notifyDataSetChanged();
                            }
                        } else {

                            // Create a new HistorialItem with Recolector data
                            HistorialItem newItem = new HistorialItem(squareDrawable, date, time, materialsInfo, estado, color, isRated, id, null, timeStamp);

                            itemList.add(newItem);
                            historialAdapter.notifyDataSetChanged();
                        }
                    } else {
                        // Handle any errors or exceptions related to retrieving the document
                        Log.d("firestorageRecolectorError", "Couldn't access fireStorage DB");
                    }
                });
            } else {


                // Create a new HistorialItem with Recolector data
                HistorialItem newItem = new HistorialItem(squareDrawable, date, time, materialsInfo, estado, color, isRated, id, null, timeStamp);
                itemList.add(newItem);
                historialAdapter.notifyDataSetChanged();
            }
        }
    }


    // Find the index of an item by ID
    private int findItemIndexById(String id) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    // Implement a function to map estado to color
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

    // Implement a function to map estado to Drawable
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

    public void addOrder (View v) {
        Intent intent = new Intent(this, OrdenHorarioActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v, int pos) {

        HistorialItem clickedItem = itemList.get(pos);
        String estado = clickedItem.getEstado();

        showCorrectPopup(estado, pos);
    }


    public void showCorrectPopup(String estado, int itemPos) {

        if(estado.equals(this.iniciada)) {

            // Create a view for the semitransparent background
            final View backgroundView = new View(this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme); // Apply the custom theme

            // Inflate the custom layout
            View dialogView = getLayoutInflater().inflate(R.layout.historial_popup_iniciada, null);

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
            Button btnCancelarOrden = dialogView.findViewById(R.id.cancelarOrdenButton);
            Button btnContinuar = dialogView.findViewById(R.id.continuarButton);

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

            // Add the background view and show the dialog
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.addView(backgroundView);
        }

        else if(estado.equals(this.enProceso)) {
            // Create a view for the semitransparent background
            final View backgroundView = new View(this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme); // Apply the custom theme

            // Inflate the custom layout
            View dialogView = getLayoutInflater().inflate(R.layout.historial_popup_en_proceso, null);

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

            // Se agrega el nombre del recolector
            TextView recolector = dialogView.findViewById(R.id.recolector);
            recolector.setText(itemList.get(itemPos).getRecolector().generarNombreCompleto());

            // Se agrega la imagen del recolector
            ImageFilterView recolectorImg = dialogView.findViewById(R.id.recolectorImg);
            String url = "https://storage.cloud.google.com/artifactory-images/old_fashioned_ring.PNG";
            Picasso.get().load(url).into(recolectorImg);


            // Se agrega el teléfono del recolector
            TextView recolectorTelefono = dialogView.findViewById(R.id.recolectorTelefono);
            recolectorTelefono.setText(itemList.get(itemPos).getRecolector().getTelefono());

            // Configure button actions
            Button btnContinuar = dialogView.findViewById(R.id.continuarButton);

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

            // Create a view for the semitransparent background
            final View backgroundView = new View(this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme); // Apply the custom theme

            // Inflate the custom layout
            View dialogView = getLayoutInflater().inflate(R.layout.historial_popup_completada, null);

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

            // Set the correct name for the recolector
            TextView recolector = dialogView.findViewById(R.id.recolector);
            recolector.setText(itemList.get(itemPos).getRecolector().generarNombreCompleto());

            // Configure button actions
            LinearLayout linearLayoutRatingSection = dialogView.findViewById(R.id.linearLayoutRating);
            RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
            Button btnContinuar = dialogView.findViewById(R.id.continuarButton);

            if (itemList.get(itemPos).getIsRated()) {
                linearLayoutRatingSection.setVisibility(View.GONE);
                Resources res = getResources();
                Drawable button_variant_green = ResourcesCompat.getDrawable(res, R.drawable.button_variant_green, null);
                btnContinuar.setBackground(button_variant_green);
                btnContinuar.setEnabled(true);
            }

            ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
                Resources res = getResources();
                Drawable button_variant_green = ResourcesCompat.getDrawable(res, R.drawable.button_variant_green, null);
                btnContinuar.setBackground(button_variant_green);
                btnContinuar.setEnabled(true);
            });

            btnContinuar.setOnClickListener(v -> {
                if (!itemList.get(itemPos).getIsRated()) {
                    Toast.makeText(this,
                            "DB changed!!!",
                            Toast.LENGTH_LONG).show();
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

            // Add the background view and show the dialog
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.addView(backgroundView);
        }

        else if(estado.equals(this.cancelada)) {

            // Create a view for the semitransparent background
            final View backgroundView = new View(this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme); // Apply the custom theme

            // Inflate the custom layout
            View dialogView = getLayoutInflater().inflate(R.layout.historial_popup_cancelada, null);

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
            Button btnVerCentros = dialogView.findViewById(R.id.verCentrosButton);
            Button btnContinuar = dialogView.findViewById(R.id.continuarButton);

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

            // Add the background view and show the dialog
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.addView(backgroundView);
        }

    }

    private void updateRecoleccionEstado(String recoleccionIdToUpdate, String newEstado) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference recoleccionRef = databaseReference.child("recolecciones");
        // Obtain the reference to the specific recolección using its unique 'rid'
        Query query = recoleccionRef.orderByChild("rid").equalTo(recoleccionIdToUpdate);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Update the 'estado' field with the new value
                    snapshot.getRef().child("estado").setValue(newEstado);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
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
}