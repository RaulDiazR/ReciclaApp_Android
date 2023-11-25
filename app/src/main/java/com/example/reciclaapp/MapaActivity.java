package com.example.reciclaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MapaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // Set item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.mapa) {
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
        } else if (itemId == R.id.reciclaje) {
            intent = new Intent(this, HistorialRecoleccionesActivity.class);
        } else if (itemId == R.id.ajustes) {
            intent = new Intent(this, SettingsActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    private int getCurrentItemIdForActivity() {
        Class<?> currentClass = this.getClass();

        if (currentClass == VerNoticiasActivity.class) {
            return R.id.inicio;
        } else if (currentClass == HistorialRecoleccionesActivity.class) {
            return R.id.reciclaje;
        } else if (currentClass == SettingsActivity.class) {
            return R.id.ajustes;
        } else {
            return R.id.mapa;
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
}
