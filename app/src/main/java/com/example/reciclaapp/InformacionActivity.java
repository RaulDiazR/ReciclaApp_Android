package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InformacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        // Find the Toolbar by its ID
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the Toolbar as the app bar
        setSupportActionBar(toolbar);

        // Remove default title for app bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Enable the back button (up navigation)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void goToIniciativa(View v) {
        Intent intent = new Intent(this, IniciativaActivity.class);
        startActivity(intent);
    }

    public void goToTyC(View v) {
        Intent intent = new Intent(this, TerminosCondicionesActivity.class);
        startActivity(intent);
    }

    public void goToPolPriv(View v) {
        Intent intent = new Intent(this, AvisoprivacidadActivity.class);
        startActivity(intent);
    }
}