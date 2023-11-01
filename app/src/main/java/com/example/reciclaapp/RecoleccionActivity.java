package com.example.reciclaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.navigation.NavigationBarView;

public class RecoleccionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoleccion);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.reciclaje);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.inicio) {
                    // Navegar a la actividad de News
                    Intent intent = new Intent(RecoleccionActivity.this, NewsActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.mapa) {
                    // Navegar a la actividad de Mapa
                    Intent intent2 = new Intent(RecoleccionActivity.this, MapaActivity.class);
                    startActivity(intent2);
                } else if (item.getItemId() == R.id.reciclaje) {
                    // Navegar a la actividad de Recolección
                    Intent intent3 = new Intent(RecoleccionActivity.this, RecoleccionActivity.class);
                    startActivity(intent3);
                } else if (item.getItemId() == R.id.ajustes) {
                    // Navegar a la actividad de Settings
                    Intent intent4 = new Intent(RecoleccionActivity.this, SettingsActivity.class);
                    startActivity(intent4);
                }
                return true;
            }
        });
    }
}