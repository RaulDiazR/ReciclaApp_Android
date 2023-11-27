package com.example.reciclaapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {
    public static final int CAMERA_PERMISSION_REQUEST = 1001;
    public static final int GALLERY_PERMISSION_REQUEST = 1;
    String currentPhotoPath;
    FirebaseAuth auth;
    ImageView image;
    FirebaseUser user;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        image = findViewById(R.id.fotoperfil);

        storageReference = FirebaseStorage.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            // Initialize Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Reference to the Users collection
            DocumentReference userRef = db.collection("usuarios").document(user.getUid());

            // Get user information
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Check if the user has a profile photo
                    if (documentSnapshot.contains("fotoPerfil")) {
                        String photoUrl = documentSnapshot.getString("fotoPerfil");

                        // Load image from Firebase Storage
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            StorageReference photoRef = storageReference.child("fotosUsuarios/" + photoUrl);
                            photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Use a library like Glide or Picasso to load the image into the ImageView
                                // Here, I'm using Glide for simplicity (add the dependency to your app's build.gradle)
                                // implementation 'com.github.bumptech.glide:glide:4.12.0'
                                Glide.with(SettingsActivity.this).load(uri).into(image);
                            }).addOnFailureListener(e -> {
                                // Handle error loading image from Firebase Storage
                                Log.e("SettingsActivity", "Error loading image from Firebase Storage", e);
                            });
                        }
                    }
                }
            }).addOnFailureListener(e -> {
                // Handle error fetching user information from Firestore
                Log.e("SettingsActivity", "Error fetching user information from Firestore", e);
            });
        }

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // Set item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.ajustes) {
                // Do nothing if already on the "Mapa" page.
            } else {
                // Navigate to the corresponding activity
                navigateToActivity(itemId);
            }
            return true;
        });

        // Initialize the selected item based on the current activity
        int currentItemId = getCurrentItemIdForActivity();
        bottomNavigationView.setSelectedItemId(currentItemId);
    }

    private void navigateToActivity(int itemId) {
        Intent intent = null;

        if (itemId == R.id.inicio) {
            intent = new Intent(this, VerNoticiasActivity.class);
        } else if (itemId == R.id.mapa) {
            intent = new Intent(this, StreetMapActivity.class);
        } else if (itemId == R.id.reciclaje) {
            intent = new Intent(this, HistorialRecoleccionesActivity.class);
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    private int getCurrentItemIdForActivity() {
        Class<?> currentClass = this.getClass();

        if (currentClass == VerNoticiasActivity.class) {
            return R.id.inicio;
        } else if (currentClass == StreetMapActivity.class) {
            return R.id.mapa;
        } else if (currentClass == HistorialRecoleccionesActivity.class) {
            return R.id.reciclaje;
        } else {
            return R.id.ajustes;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Update the selected item in the bottom navigation view
        int currentItemId = getCurrentItemIdForActivity();
        bottomNavigationView.setSelectedItemId(currentItemId);
        overridePendingTransition(0,0);
    }

    public void profilePhoto(View v) {
        FirebaseUser user = auth.getCurrentUser();
        updateUI(user);
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflar el diseño personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.fotoperfil, null);

        // Configurar el diálogo
        builder.setView(dialogView);

        // Personalizar el diálogo
        final AlertDialog alertDialog = builder.create();

        // Configurar un fondo semitransparente
        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }

        alertDialog.show();

        // Configurar acciones de los botones
        Button cameraBtn = dialogView.findViewById(R.id.cameraBtn);
        Button galleryBtn = dialogView.findViewById(R.id.galleryBtn);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { dispatchPickFromGalleryIntent(); }
        });

        alertDialog.setOnDismissListener(view -> {
            alertDialog.dismiss();
            FrameLayout rootView = findViewById(android.R.id.content);
            rootView.removeView(backgroundView);
        });

        // Agregar la vista de fondo y mostrar el cuadro de diálogo
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);
        alertDialog.show();
    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }
        else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Se necesitan permisos para usar la cámara", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Se necesitan permisos para acceder a su galería", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ActivityResultLauncher<Intent> camaraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference userRef = db.collection("usuarios").document(user.getUid());
                File f = new File(currentPhotoPath);
                image.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Url of Image: "+Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getApplicationContext().sendBroadcast(mediaScanIntent);
                uploadImageToFirebase(f.getName(),contentUri);
                userRef.update(
                        "fotoPerfil", f.getName().toString()
                );
            }
        }
    });

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference userRef = db.collection("usuarios").document(user.getUid());
                Uri contentUri = result.getData().getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "."+getFileExt(contentUri);
                Log.d("tag", "Url of Image: "+imageFileName);
                image.setImageURI(contentUri);
                uploadImageToFirebase(imageFileName,contentUri);
                userRef.update(
                        "fotoPerfil", imageFileName.toString()
                );
            }
        }
    });

    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("fotosUsuarios/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "Uploaded Image URl is " + uri.toString());
                    }
                });
                Toast.makeText(SettingsActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsActivity.this, "Upload Failled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.reciclaapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                camaraLauncher.launch(takePictureIntent);
            }
        }
    }

    private void dispatchPickFromGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    public void goToPerfil(View v) {
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
    }

    public void goToFQA(View v) {
        Intent intent = new Intent(this, FQAActivity.class);
        startActivity(intent);
    }

    public void goToFavoritos(View v) {
        Intent intent = new Intent(this, FavoritosActivity.class);
        startActivity(intent);
    }

    public void goToInformacion(View v) {
        Intent intent = new Intent(this, InformacionActivity.class);
        startActivity(intent);
    }

    public void goToContacto(View v) {
        Intent intent = new Intent(this, ContactoActivity.class);
        startActivity(intent);
    }

    public void goToSociosyAmigos(View v) {
        Intent intent = new Intent(this, SociosyamigosActivity.class);
        startActivity(intent);
    }

    public void goToCreditos(View v) {
        Intent intent = new Intent(this, CreditosActivity.class);
        startActivity(intent);
    }

    private void updateUI(FirebaseUser user) {}

    // onSaveInstanceState: Método llamado para guardar el estado de la actividad antes de ser destruida.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // onRestoreInstanceState: Método llamado al restaurar el estado de la actividad después de ser recreada.
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}