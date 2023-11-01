package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class UpdatePasswordActivity extends AppCompatActivity {

    EditText contrasenaField;
    EditText nuevacontrasenaField;
    EditText confirmarcontrasenaField;

    TextView contrasenaTextView;
    TextView nuevacontrasenaTextView;
    TextView confirmarcontrasenaTextView;
    TextView errorTextView;

    String contrasenaText;
    String nuevacontrasenaText;
    String confirmarcontrasenaText;

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

        contrasenaField = findViewById(R.id.contrasenaActual);
        nuevacontrasenaField = findViewById(R.id.contrasenaNueva);
        confirmarcontrasenaField = findViewById(R.id.newPasswordCon);

        contrasenaTextView = findViewById(R.id.textView444);
        nuevacontrasenaTextView = findViewById(R.id.textView222);
        confirmarcontrasenaTextView = findViewById(R.id.textView333);
    }

    @Override

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void Actualizar(View view) {
        if (isFormValid()) {
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
                    Intent intent = new Intent(UpdatePasswordActivity.this, PerfilActivity.class);
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
    private boolean isFormValid() {
        boolean isValid = true;

        if (isEmpty(contrasenaField)) {
            contrasenaField.setError("Este campo es obligatorio");
            errorTextView = contrasenaTextView;
            isValid = false;
        }

        if (isEmpty(nuevacontrasenaField)) {
            nuevacontrasenaField.setError("Este campo es obligatorio");
            errorTextView = nuevacontrasenaTextView;
            isValid = false;
        }

        if (isEmpty(confirmarcontrasenaField)) {
            confirmarcontrasenaField.setError("Este campo es obligatorio");
            errorTextView = confirmarcontrasenaTextView;
            isValid = false;
        }

        contrasenaText = contrasenaField.getText().toString();
        if(!contrasenaText.equals("useradmin123")){
            contrasenaField.setError("La contraseña es incorrecta");
            isValid = false;
        }

        nuevacontrasenaText = nuevacontrasenaField.getText().toString();
        confirmarcontrasenaText = confirmarcontrasenaField.getText().toString();
        if(!nuevacontrasenaText.equals(confirmarcontrasenaText)){
            confirmarcontrasenaField.setError("Las contraseñas no coinciden");
            errorTextView = confirmarcontrasenaTextView;
            isValid = false;
        }

        return isValid;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
}