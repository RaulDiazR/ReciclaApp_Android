package com.example.reciclaapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reciclaapp.models.McqMaterial;
import com.example.reciclaapp.models.McqRecoleccion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MaterialesActivity extends AppCompatActivity{
    private static final int CAMERA_PERMISSION_REQUEST = 1001;
    private static final int PICK_FROM_GALLERY = 1;
    // 1- AdapterView
    RecyclerView recyclerView;
    // 2- DataSource
    private List<MaterialesItem> itemList;
    // 3- Adapters
    MaterialesHeaderAdapter headerAdapter;
    private MaterialesAdapter materialesAdapter; // Add MaterialesAdapter
    ConcatAdapter concatAdapter; // Add ConcatAdapter
    // intent with data from previous activities
    private Intent receivedIntent;
    // reference to the FireStore database and the Storage
    private DatabaseReference fbRecoleccionesRef;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    // Se usan para activar la cámara y la galería
    ActivityResultLauncher<Intent> takePhotoLauncher;
    ActivityResultLauncher<Intent> openGallery;

    public static int lastItemPosPhotoTaken;

    public static String currentPhotoPath;

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

        // Se accede a la base de datos de FireStore
        firestore = FirebaseFirestore.getInstance();
        fbRecoleccionesRef = FirebaseDatabase.getInstance().getReference("recolecciones");

        // Se accede a Firebase Storage para acceder a las imágenes
        // referente to firebase storage for images
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        System.out.print(storageReference);

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
        takePhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                int itemPos = lastItemPosPhotoTaken;
                File f = new File(currentPhotoPath);
                Uri imgUri = Uri.fromFile(f);
                if (itemPos != -1) {
                    itemList.get(itemPos).setFotoMaterial(imgUri);
                    materialesAdapter.notifyItemChanged(itemPos);

                }
            }
        });

        // Se inicializa el ActivityResultLaunches para la actividad de escoger una foto de la galería
        openGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            try {
                if (result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        int itemPos = lastItemPosPhotoTaken;

                        if (itemPos != -1) {
                            itemList.get(itemPos).setFotoMaterial(imageUri);
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
            sendDataToDB();
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

    private void sendDataToDB() {
        receivedIntent = getIntent();

        Map<String, Object> recoleccionData = new HashMap<>();

        // se extrae la fecha y se le da un formato simple de dd/mm/aa
        int[] fecha = receivedIntent.getIntArrayExtra("fecha");
        int day, month, year;
        if (fecha != null) {
            day = fecha[0];
            month = fecha[1];
            year = fecha[2];
            String fechaStr = "" + day + "/" + month + "/" + year;
            recoleccionData.put("fechaRecoleccion", fechaStr);
        }
        // Se extraen los tiempos de inicio y final de la recolección
        int[] tiempoEnd = receivedIntent.getIntArrayExtra("tiempoEnd");
        if (tiempoEnd != null) {
        String timeEnd = String.format(Locale.getDefault(), "%02d", tiempoEnd[0]) + ":" + String.format(Locale.getDefault(), "%02d", tiempoEnd[1]);
            recoleccionData.put("horaRecoleccionFinal",timeEnd);
        }
        int[] tiempoIni = receivedIntent.getIntArrayExtra("tiempoIni");
        if (tiempoIni != null) {
            String timeIni = String.format(Locale.getDefault(), "%02d", tiempoIni[0]) + ":" + String.format(Locale.getDefault(), "%02d", tiempoIni[1]);
            recoleccionData.put("horaRecoleccionInicio",timeIni);
        }
        // se extraen los comentarios
        String comentarios = receivedIntent.getStringExtra("comentarios");
        if (comentarios != null) {
            recoleccionData.put("comentarios", comentarios);
        }
        // Se extrae el valor booleano de si el pedido es en puerte o no
        boolean enPersona = receivedIntent.getBooleanExtra("enPersona", true);
        recoleccionData.put("enPersona",enPersona);

        // Add all the remaining values which are constants
        Long timeStamp = System.currentTimeMillis(); // Get the current timestamp
        recoleccionData.put("timeStamp",timeStamp); // Add the timestamp to your data
        recoleccionData.put("idUsuarioCliente", "user_id_1"); // cambiar por el id del usuario
        recoleccionData.put("idRecolector", ""); // se pone vacío, pues al principio la recolección no tiene recolector
        recoleccionData.put("calificado", false);
        recoleccionData.put("estado", "Iniciada");

        // crear Map de materiales viejo
        /*
        // Create a map to hold material data
        Map<String, McqMaterial> materiales = new HashMap<>();
        for (int count = 0; count < itemList.size(); count++) {
            MaterialesItem materialesItem = itemList.get(count);

            McqMaterial material = new McqMaterial();
            material.setNombre(materialesItem.getName());
            material.setUnidad(materialesItem.getMaterialUnit());
            material.setCantidad(materialesItem.materialQuantity);

            // Upload the image to Firebase Storage and set the URL
            if (materialesItem.getFotoMaterial() != null) {
                uploadPictureToFirebase(materialesItem.getFotoMaterial(), count, uid);
            }
            else {
                material.setFotoUrl("");
            }

            materiales.put("material_" + count, material);
        } */
        Map<String, Map<String, Object>> materialesMap = new HashMap<>();

        for (int count = 0; count < itemList.size(); count++) {
            MaterialesItem materialesItem = itemList.get(count);
            Map<String, Object> materialData = new HashMap<>();
            materialData.put("nombre", materialesItem.getName());
            materialData.put("unidad", materialesItem.getMaterialUnit());
            materialData.put("cantidad", materialesItem.getMaterialQuantity());
            materialData.put("fotoUrl", "");

            String materialId = "material_" + count;
            materialesMap.put(materialId, materialData);
        }

        recoleccionData.put("materiales", materialesMap);


        // Add a new recolección document to the "recolecciones" collection
        firestore.collection("recolecciones")
                .add(recoleccionData)
                .addOnSuccessListener(documentReference -> {
                    // Handle success, documentReference.getId() is the unique ID of the added document.
                    String recoleccionId = documentReference.getId();

                    // After the recolección document is added, you can update it with the photo URL.
                    for (int count = 0; count < itemList.size(); count++) {
                        MaterialesItem materialesItem = itemList.get(count);
                        if (materialesItem.getFotoMaterial() != null) {
                            uploadPictureToFirebase(materialesItem.getFotoMaterial(), count, recoleccionId);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure.
                });
    }


    private void uploadPictureToFirebase(Uri imageUri, int count, String recoleccionId) {
        final String randomKey = UUID.randomUUID().toString();
        StorageReference imagesRef = storageReference.child("fotosMateriales/" + randomKey);

        imagesRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageURL = uri.toString();
            setFotoEvidenciaForItem(imageURL, count, recoleccionId); // Set the URL after it's obtained
            // You can also save the material to the database here if needed
        })).addOnFailureListener(e -> Log.d("Firebase", "No se pudo subir la foto" + imageUri));
    }

    private void setFotoEvidenciaForItem(String imageURL, int count, String recoleccionId) {
        if (count >= 0 && count < itemList.size()) {
            itemList.get(count).setUrlFotoMaterial(imageURL);
            materialesAdapter.notifyItemChanged(count);
            // Save the material to the database here if needed
            updateMaterialWithFotoUrl(imageURL, count, recoleccionId);
        }
    }

    private void updateMaterialWithFotoUrl(String imageURL, int count, String recoleccionId) {

        // Initialize a Firestore DocumentReference for the recolección document
        DocumentReference recoleccionRef = firestore.collection("recolecciones").document(recoleccionId);

        // Update a specific material (e.g., "material_0") within the recolección document
        String materialKey = "material_"+count; // Replace with the actual material key

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("materiales." + materialKey + ".fotoUrl", imageURL);

        recoleccionRef.update(updateData)
                .addOnSuccessListener(aVoid -> {
                    // The "unidad" field of the specific material has been successfully updated.
                })
                .addOnFailureListener(e -> {
                    Log.d("FirebaseFailureUpdateImageMaterial", "Image could not be uploaded");
                });
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, you can now launch the camera intent.
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    }
                    catch (IOException ex) {
                        // Error occurred while creating the File
                    ex.printStackTrace();
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.example.reciclaapp.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        takePhotoLauncher.launch(takePictureIntent);
                    }
                }
            }
        }

        else if (requestCode == PICK_FROM_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Gallery permission has been granted, you can now launch the gallery intent.
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                openGallery.launch(openGalleryIntent);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<MaterialesItem> itemListToSave = new ArrayList<>(itemList);
        outState.putParcelableArrayList("itemList", itemListToSave);
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ArrayList<MaterialesItem> restoredItemList = savedInstanceState.getParcelableArrayList("itemList");
        if (restoredItemList != null) {
            itemList.clear();
            itemList.addAll(restoredItemList);
            // Notify the adapter if needed
            if (materialesAdapter != null) {
                materialesAdapter.notifyDataSetChanged();
            }
        }
    }



}
