package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void goToPerfil(View v) {
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
    }

    public void goToConfiguracion(View v) {
        Intent intent = new Intent(this, ConfiguracionActivity.class);
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

    public void goToAvisoPriv(View v) {
        Intent intent = new Intent(this, AvisoprivacidadActivity.class);
        startActivity(intent);
    }

    public void goToCreditos(View v) {
        Intent intent = new Intent(this, CreditosActivity.class);
        startActivity(intent);
    }
}