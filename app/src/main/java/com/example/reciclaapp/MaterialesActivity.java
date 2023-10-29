package com.example.reciclaapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MaterialesActivity extends AppCompatActivity implements MaterialesAdapter.OnItemRemovedListener {
    // 1- AdapterView
    RecyclerView recyclerView;
    // 2- DataSource
    List<MaterialesItem> itemList;
    // 3- Adapters
    MaterialesHeaderAdapter headerAdapter;
    MaterialesAdapter materialesAdapter; // Add MaterialesAdapter
    ConcatAdapter concatAdapter; // Add ConcatAdapter
    // Replace this line with the request code for camera capture
    // You can use any unique value
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private CameraIntentHelper cameraIntentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiales);

        // Find the Toolbar by its ID and set the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for the app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Get result from MaterialesSelectionActivity
        ActivityResultLauncher<Intent> materialesActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        int imageResource = data.getIntExtra("imageResource", 0);
                        String text = data.getStringExtra("text");

                        // Add the chosen material to your data source
                        itemList.add(new MaterialesItem(imageResource, text));

                        // Notify the adapter that an item has been inserted at the last position
                        int insertedPosition = itemList.size() - 1;
                        materialesAdapter.notifyItemInserted(insertedPosition);
                    }
                });

        cameraIntentHelper = new CameraIntentHelper(this);

        recyclerView = findViewById(R.id.recyclerViewMateriales);

        String[] infoFromIntent = getInfoFromIntent();
        // Create a MaterialesHeaderItem instance with the necessary data
        MaterialesHeaderItem headerData = new MaterialesHeaderItem(infoFromIntent[0], infoFromIntent[1]);

        // Create an instance of MaterialesHeaderAdapter and provide the headerData
        headerAdapter = new MaterialesHeaderAdapter(this, headerData, materialesActivityResultLauncher);

        itemList = new ArrayList<>();
        // Add the chosen material to your data source
        itemList.add(new MaterialesItem(R.drawable.material_madera, "Madera"));
        materialesAdapter = new MaterialesAdapter(this, itemList);

        // Create a ConcatAdapter and add the headerAdapter and materialesAdapter
        concatAdapter = new ConcatAdapter(headerAdapter, materialesAdapter);

        // Create and set the linear layout manager so the recycler view works properly like a scrolling view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Set the ConcatAdapter as the adapter for the RecyclerView
        recyclerView.setAdapter(concatAdapter);

    }

    private String[] getInfoFromIntent() {
        Intent receivedIntent = getIntent();
        int[] fecha = receivedIntent.getIntArrayExtra("fecha");
        int day, month, year;
        assert fecha != null;
        day = fecha[0];
        month = fecha[1];
        year = fecha[2];
        String fechaStr = formatDate(day, month, year);

        int[] tiempoEnd = receivedIntent.getIntArrayExtra("tiempoEnd");
        assert tiempoEnd != null;
        String formattedTime = String.format(Locale.getDefault(),"%02d", tiempoEnd[0]) + ":" + String.format(Locale.getDefault(),"%02d", tiempoEnd[1]);

        return new String[]{fechaStr, formattedTime};
    }

    private String formatDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); // Subtract 1 from the month since Calendar uses 0-based indexing.
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d 'de' MMM 'de' yyyy", new Locale("es", "MX"));
        return dateFormat.format(cal.getTime());
    }


    @Override
    public void onItemRemoved(int position) {
        // Handle item removal here (e.g., update your data model)
    }

    public void finishMaterialSelection(View v) {
        if (materialesAdapter.getItemCount() > 0) {
            confirmarOrden(v);
        }
        else {
            Toast.makeText(this, "Debe seleccionar mínimo 1 material",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void confirmarOrden(View view) {
        // Create a view for the semitransparent background
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme); // Apply the custom theme

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.materiales_confirmar_orden, null);

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
            mostrarConfirmacion(v);
            alertDialog.dismiss();
        });

        btnCancelar.setOnClickListener(v -> {
            // Logic for Cancel
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

    public void mostrarConfirmacion(View view) {
        // Create a view for the semitransparent background
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Semitransparent color

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialogTheme); // Apply the custom theme

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.materiales_mostrar_confirmacion, null);

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

        btnConfirmar.setOnClickListener(v -> finishOrder());

        alertDialog.setOnDismissListener(v -> finishOrder());

        // Add the background view and show the dialog
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);
    }

    private void finishOrder() {
        // Logic for Confirm
        Intent intent = new Intent(MaterialesActivity.this, HistorialRecoleccionesActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Se pasa la información de la actividad previa
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        //intent.pu

        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                cameraIntentHelper.onActivityResult(requestCode, resultCode, data);
                materialesAdapter.notifyDataSetChanged();
            }
        }
    }

}
