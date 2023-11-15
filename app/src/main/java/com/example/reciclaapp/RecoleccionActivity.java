package com.example.reciclaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class RecoleccionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoleccion);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // Set item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.reciclaje) {
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

        if (currentClass == NewsActivity.class) {
            return R.id.inicio;
        } else if (currentClass == MapaActivity.class) {
            return R.id.mapa;
        } else if (currentClass == SettingsActivity.class) {
            return R.id.ajustes;
        } else {
            return R.id.reciclaje;
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
