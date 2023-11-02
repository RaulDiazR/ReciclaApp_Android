package com.example.reciclaapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import com.example.reciclaapp.models.McqMaterial;
import com.example.reciclaapp.models.McqRecoleccion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    Intent receivedIntent;
    DatabaseReference fbRecoleccionesRef;

    public static int lastItemPosPhotoTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiales);
        // Find the Toolbar by its ID and set the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for the app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // creates intent to receive data from past activities
        this.receivedIntent = getIntent();

        // Se accede a la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        fbRecoleccionesRef = database.getReference("recolecciones");

        FirebaseStorage fbStorage = FirebaseStorage.getInstance();
        StorageReference fbStorageReference = fbStorage.getReference();
        System.out.print(fbStorageReference);

        /*
        // -------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------
        DatabaseReference recolectoresRef = database.getReference("recolectores");
        // se genera un id único para la recolección
        String uid = recolectoresRef.push().getKey();

        McqRecolector recolector = new McqRecolector();
        recolector.setId(uid);
        recolector.setApellidos("Alden Armstrong");
        recolector.setTelefono("999 999 9999");
        recolector.setNombre("Neil");

        // Write a message to the database
        if (uid != null) {
            recolectoresRef.child(uid).setValue(recolector).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(MaterialesActivity.this, "Lo sentimos, no se pudo procesar su recolector JAJAJJAJAJJA!",
                            Toast.LENGTH_LONG).show();
                }
            });
        } */

        // Get result from MaterialesSelectionActivity
        ActivityResultLauncher<Intent> materialesActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            int imageResource = data.getIntExtra("imageResource", 0);
                            String text = data.getStringExtra("text");


                            // Add the chosen material to your data source
                            itemList.add(new MaterialesItem(imageResource, text));

                            // Notify the adapter that an item has been inserted at the last position
                            int insertedPosition = itemList.size() - 1;
                            materialesAdapter.notifyItemInserted(insertedPosition);
                        }
                    }
                });

        recyclerView = findViewById(R.id.recyclerViewMateriales);

        String[] infoFromIntent = getInfoFromIntent();
        // Create a MaterialesHeaderItem instance with the necessary data
        MaterialesHeaderItem headerData = new MaterialesHeaderItem(infoFromIntent[0], infoFromIntent[1]);

        // Create an instance of MaterialesHeaderAdapter and provide the headerData
        headerAdapter = new MaterialesHeaderAdapter(this, headerData, materialesActivityResultLauncher);

        itemList = new ArrayList<>();

        // Se inicializa el ActivityResultLaunches para la actividad de tomar una foto
        ActivityResultLauncher<Intent> takePhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        Bitmap photoBitmap = (Bitmap) extras.get("data");
                        // Retrieve the item position from the intent's extra
                        Log.d("CameraResult", "Extras: " + Objects.requireNonNull(result.getData().getExtras()));
                        int itemPos = lastItemPosPhotoTaken;

                        if (itemPos != -1 && photoBitmap != null) {
                            itemList.get(itemPos).setFotoMaterial(photoBitmap);
                            materialesAdapter.notifyItemChanged(itemPos);
                        }
                    }
                }
            }
        });

        // Se inicializa el ActivityResultLaunches para la actividad de escoger una foto de la galería
        ActivityResultLauncher<Intent> openGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            try {
                if (result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        int itemPos = lastItemPosPhotoTaken;

                        if (itemPos != -1 && photoBitmap != null) {
                            itemList.get(itemPos).setFotoMaterial(photoBitmap);
                            materialesAdapter.notifyItemChanged(itemPos);

                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "No se seleccionó ninguna Imagen", Toast.LENGTH_LONG).show();
            }
        });

        FrameLayout rootView = findViewById(android.R.id.content);
        itemList.add(new MaterialesItem(R.drawable.material_madera, "Madera"));
        materialesAdapter = new MaterialesAdapter(this, itemList, takePhotoLauncher, openGallery, rootView);

        // Create a ConcatAdapter and add the headerAdapter and materialesAdapter
        concatAdapter = new ConcatAdapter(headerAdapter, materialesAdapter);

        // Create and set the linear layout manager so the recycler view works properly like a scrolling view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Set the ConcatAdapter as the adapter for the RecyclerView
        recyclerView.setAdapter(concatAdapter);

    }

    private String[] getInfoFromIntent() {
        receivedIntent = getIntent();
        int[] fecha = receivedIntent.getIntArrayExtra("fecha");
        String fechaStr;
        int day, month, year;
        int[] tiempoEnd = receivedIntent.getIntArrayExtra("tiempoEnd");
        String formattedTime;
        if (fecha != null && tiempoEnd != null) {
            day = fecha[0];
            month = fecha[1];
            year = fecha[2];
            fechaStr = formatDate(day, month, year);
            formattedTime = String.format(Locale.getDefault(), "%02d", tiempoEnd[0]) + ":" + String.format(Locale.getDefault(), "%02d", tiempoEnd[1]);
        }
        else {
            fechaStr = getString(R.string.fecha);
            formattedTime = getString(R.string._12_00_hrs);
        }

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
            sendDataToDB();
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

    private void sendDataToDB() {
        receivedIntent = getIntent();
        McqRecoleccion recoleccion = new McqRecoleccion();

        // se extrae la fecha y se le da un formato simple de dd/mm/aa
        int[] fecha = receivedIntent.getIntArrayExtra("fecha");
        int day, month, year;
        if (fecha != null) {
            day = fecha[0];
            month = fecha[1];
            year = fecha[2];
            String fechaStr = "" + day + "/" + month + "/" + year;
            recoleccion.setFechaRecoleccion(fechaStr);
        }

        // Se extraen los tiempos de inicio y final de la recolección
        int[] tiempoEnd = receivedIntent.getIntArrayExtra("tiempoEnd");
        if (tiempoEnd != null) {
        String timeEnd = String.format(Locale.getDefault(), "%02d", tiempoEnd[0]) + ":" + String.format(Locale.getDefault(), "%02d", tiempoEnd[1]);
            recoleccion.setHoraRecoleccionFinal(timeEnd);
        }

        int[] tiempoIni = receivedIntent.getIntArrayExtra("tiempoIni");
        if (tiempoIni != null) {
            String timeIni = String.format(Locale.getDefault(), "%02d", tiempoIni[0]) + ":" + String.format(Locale.getDefault(), "%02d", tiempoIni[1]);
            recoleccion.setHoraRecoleccionInicio(timeIni);
        }

        // se extraen los comentarios
        String comentarios = receivedIntent.getStringExtra("comentarios");
        if (comentarios != null) {
            boolean enPersona = receivedIntent.getBooleanExtra("enPersona", true);
            recoleccion.setEnPersona(enPersona);
        }

        // se genera un id único para la recolección
        String uid = fbRecoleccionesRef.push().getKey();


        // Add a timestamp for when the data was sent
        Long timeStamp = System.currentTimeMillis(); // Get the current timestamp
        recoleccion.setTimeStamp(timeStamp); // Add the timestamp to your data
        recoleccion.setRid(uid);
        recoleccion.setIdUsuarioCliente("user_id_1"); // cambiar por el id del usuario
        recoleccion.setIdRecolector(""); // se pone vacío, pues al principio la recolección no tiene recolector
        recoleccion.setComentarios(comentarios);
        recoleccion.setRated(false);
        recoleccion.setEstado("Iniciada");

// Create a map to hold material data
        Map<String, McqMaterial> materiales = new HashMap<>();
        int count = 0;
        for (MaterialesItem materialesItem : itemList) {
            McqMaterial material1 = new McqMaterial();
            material1.setNombre(materialesItem.getName());
            material1.setUnidad(materialesItem.getMaterialUnit());
            material1.setCantidad(materialesItem.materialQuantity);
            material1.setFotoEvidencia("base64_encoded_image_"+count);
            materiales.put("material_"+count, material1);
            count++;
        }

        recoleccion.setMateriales(materiales);

        // Write a message to the database
        if (uid != null) {
            fbRecoleccionesRef.child(uid).setValue(recoleccion).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(MaterialesActivity.this, "Lo sentimos, no se pudo procesar su orden. Por favor, inténtelo de nuevo.",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void finishOrder() {
        // Logic for Confirm
        Intent intent = new Intent(MaterialesActivity.this, HistorialRecoleccionesActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Se pasa la información de la actividad previa
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        startActivity(intent);
    }

}
