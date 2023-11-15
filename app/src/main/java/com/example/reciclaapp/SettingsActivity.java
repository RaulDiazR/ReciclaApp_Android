package com.example.reciclaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.textView11);
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
                    // Populate EditText fields with user information
                    textView.setText("Hola " + documentSnapshot.getString("nombre_s"));
                }
            }).addOnFailureListener(e -> {
                // Handle failure, if any
                Toast.makeText(SettingsActivity.this, "Error loading user information", Toast.LENGTH_SHORT).show();
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
            intent = new Intent(this, NewsActivity.class);
        } else if (itemId == R.id.mapa) {
            intent = new Intent(this, MapaActivity.class);
        } else if (itemId == R.id.reciclaje) {
            intent = new Intent(this, RecoleccionActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    private int getCurrentItemIdForActivity() {
        Class<?> currentClass = this.getClass();

        if (currentClass == NewsActivity.class) {
            return R.id.inicio;
        } else if (currentClass == MapaActivity.class) {
            return R.id.mapa;
        } else if (currentClass == RecoleccionActivity.class) {
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
}