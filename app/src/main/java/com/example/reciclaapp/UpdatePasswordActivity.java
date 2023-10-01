package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

public class UpdatePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

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
        onBackPressed();
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
        return true;
    }

    public void Actualizar(View v){
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
    }
}