package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

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
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void updatePassword(View v){
        Intent intent = new Intent(this, UpdatePasswordActivity.class);
        startActivity(intent);
    }

    public void logOut(View v){
        // Crear una vista para el fondo semitransparente
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflar el diseño personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.cerrarsesion, null);

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
        Button btnConfirmar = dialogView.findViewById(R.id.ConfirmarButton);
        Button btnCancelar = dialogView.findViewById(R.id.CancelarButton);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para Confirmar
                Intent intent = new Intent(PerfilActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para Cancelar
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView);
            }
        });

        // Agregar la vista de fondo y mostrar el cuadro de diálogo
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);
        alertDialog.show();
    }

    public void deleteAccount(View v){
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflar el diseño personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.eliminarcuenta, null);

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
        Button btnConfirmar = dialogView.findViewById(R.id.ConfirmarButton);
        Button btnCancelar = dialogView.findViewById(R.id.CancelarButton);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                View dialogView = getLayoutInflater().inflate(R.layout.eliminatucuenta, null);

                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button btnEnviar = dialogView.findViewById(R.id.EnviarCodigo);
                btnEnviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText codigoField = dialogView.findViewById(R.id.codigoNumber);
                        String codigoText = codigoField.getText().toString();

                        if (codigoText.equals("12345")){
                            alertDialog.dismiss();

                            View dialogView = getLayoutInflater().inflate(R.layout.cuentaeliminada, null);

                            builder.setView(dialogView);

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            Button btnContinuar = dialogView.findViewById(R.id.ContinuarCA);
                            btnContinuar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                        else {
                            codigoField.setError("Código Incorrecto");
                        }
                    }
                });
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para Cancelar
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView);
            }
        });

        // Agregar la vista de fondo y mostrar el cuadro de diálogo
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);
        alertDialog.show();
    }

    public void GuardarCambios(View view) {
        // Crear una vista para el fondo semitransparente
        final View backgroundView = new View(this);
        backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflar el diseño personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.guardarcambios, null);

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
        Button btnConfirmar = dialogView.findViewById(R.id.ConfirmarButton);
        Button btnCancelar = dialogView.findViewById(R.id.CancelarButton);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para Confirmar
                Intent intent = new Intent(PerfilActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para Cancelar
                alertDialog.dismiss();
                FrameLayout rootView = findViewById(android.R.id.content);
                rootView.removeView(backgroundView);
            }
        });

        // Agregar la vista de fondo y mostrar el cuadro de diálogo
        FrameLayout rootView = findViewById(android.R.id.content);
        rootView.addView(backgroundView);
        alertDialog.show();
    }
}