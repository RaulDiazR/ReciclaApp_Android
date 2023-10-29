package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_recolecciones);

        // Find the Toolbar by its ID and et the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recyclerViewHistorial);

        itemList = new ArrayList<>();
        Resources res = getResources();
        Drawable green_square = ResourcesCompat.getDrawable(res, R.drawable.shape_square_green, null);
        Drawable red_square = ResourcesCompat.getDrawable(res, R.drawable.shape_square_red, null);
        Drawable gold_square = ResourcesCompat.getDrawable(res, R.drawable.shape_square_gold, null);
        Drawable blue_square = ResourcesCompat.getDrawable(res, R.drawable.shape_square_blue, null);

        int green = ResourcesCompat.getColor(res, R.color.green, null);
        int red = ResourcesCompat.getColor(res, R.color.red, null);
        int gold = ResourcesCompat.getColor(res, R.color.golden, null);
        int blue = ResourcesCompat.getColor(res, R.color.blue, null);

        HistorialItem item2 = new HistorialItem(gold_square, "25/09/2023", "16:10","3 materiales",this.iniciada, gold, false);
        HistorialItem item3 = new HistorialItem(blue_square, "25/09/2023", "10:10","5 materiales",this.enProceso, blue, false);
        HistorialItem item4 = new HistorialItem(green_square, "29/09/2023", "09:08","2 materiales",this.completada, green, false);
        HistorialItem item5 = new HistorialItem(red_square, "25/09/2023", "15:10","4 materiales",this.cancelada, red, false);
        HistorialItem item6 = new HistorialItem(green_square, "29/09/2023", "09:08","2 materiales",this.completada, green, true);


        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
        itemList.add(item5);
        itemList.add(item6);

        // create and set the linear layout manager so the recycler view works properly like a scrolling view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        historialAdapter = new HistorialAdapter(itemList);
        recyclerView.setAdapter(historialAdapter);

        historialAdapter.setClickListener(this);

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

            TextView recolector = dialogView.findViewById(R.id.recolector);
            recolector.setText(R.string.juan_ramirez_ramirez);

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
                Resources res = getResources();
                itemList.get(itemPos).setEstado(this.cancelada);
                itemList.get(itemPos).setEstadoColor(ResourcesCompat.getColor(res, R.color.red, null));
                Drawable red_square = ResourcesCompat.getDrawable(res, R.drawable.shape_square_red, null);
                itemList.get(itemPos).setBackgroundDrawable(red_square);
                historialAdapter.notifyItemChanged(itemPos);
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