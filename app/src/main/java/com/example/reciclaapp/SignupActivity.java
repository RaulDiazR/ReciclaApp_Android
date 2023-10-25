package com.example.reciclaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class SignupActivity extends AppCompatActivity {

    EditText nombresField;
    EditText apellidosField;
    EditText correoField;
    EditText telefonoField;
    EditText fechaField;
    EditText contrasenaField;
    EditText confirmarcontrasenaField;

    TextView nombresTextView;
    TextView apellidosTextView;
    TextView correoTextView;
    TextView telefonoTextView;
    TextView fechaTextView;
    TextView contrasenaTextView;
    TextView confirmarcontrasenaTextView;
    TextView errorTextView;

    String contrasenaText;
    String confirmarcontrasenaText;

    CheckBox tycCheckBox;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Find the Toolbar by its ID
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the Toolbar as the app bar
        setSupportActionBar(toolbar);

        // Remove default title for app bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Enable the back button (up navigation)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombresField = findViewById(R.id.nombres);
        apellidosField = findViewById(R.id.apellidos);
        correoField = findViewById(R.id.mail);
        telefonoField = findViewById(R.id.phone);
        fechaField = findViewById(R.id.birthday);
        contrasenaField = findViewById(R.id.password);
        confirmarcontrasenaField = findViewById(R.id.cpassword);

        nombresTextView = findViewById(R.id.textView4);
        apellidosTextView = findViewById(R.id.textView2);
        correoTextView = findViewById(R.id.textView3);
        telefonoTextView = findViewById(R.id.textView6);
        fechaTextView = findViewById(R.id.textView7);
        contrasenaTextView = findViewById(R.id.textView8);
        confirmarcontrasenaTextView = findViewById(R.id.textView9);

        tycCheckBox = findViewById(R.id.checkBox1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return true;
    }

    public void goToLogin(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void Continuar (View v){
        if (isFormValid()) {
            // Crear una vista para el fondo semitransparente
            final View backgroundView = new View(this);
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Inflar el diseño personalizado
            View dialogView = getLayoutInflater().inflate(R.layout.confirmarregistro, null);

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

                    View dialogView = getLayoutInflater().inflate(R.layout.activatucuenta, null);

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

                                View dialogView = getLayoutInflater().inflate(R.layout.cuentaactivada, null);

                                builder.setView(dialogView);

                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                                Button btnContinuar = dialogView.findViewById(R.id.ContinuarCA);
                                btnContinuar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SignupActivity.this, SettingsActivity.class);
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
    }

    private boolean isFormValid() {
        boolean isValid = true;

        if (isEmpty(nombresField)) {
            nombresField.setError("Este campo es obligatorio");
            errorTextView = nombresTextView;
            isValid = false;
        }

        if (isEmpty(apellidosField)) {
            apellidosField.setError("Este campo es obligatorio");
            errorTextView = apellidosTextView;
            isValid = false;
        }

        if (isEmpty(correoField)) {
            correoField.setError("Este campo es obligatorio");
            errorTextView = correoTextView;
            isValid = false;
        }

        if (isEmpty(telefonoField)) {
            telefonoField.setError("Este campo es obligatorio");
            errorTextView = telefonoTextView;
            isValid = false;
        }

        if (isEmpty(fechaField)) {
            fechaField.setError("Este campo es obligatorio");
            errorTextView = fechaTextView;
            isValid = false;
        }

        if (isEmpty(contrasenaField)) {
            contrasenaField.setError("Este campo es obligatorio");
            errorTextView = contrasenaTextView;
            isValid = false;
        }

        if (isEmpty(confirmarcontrasenaField)) {
            confirmarcontrasenaField.setError("Este campo es obligatorio");
            errorTextView = confirmarcontrasenaTextView;
            isValid = false;
        }

        if (!(tycCheckBox.isChecked())) {
            tycCheckBox.setError("Este campo es obligatorio");
            //errorTextView = tycCheckBox;
            isValid = false;
        }

        contrasenaText = contrasenaField.getText().toString();
        confirmarcontrasenaText = confirmarcontrasenaField.getText().toString();
        if(!contrasenaText.equals(confirmarcontrasenaText)){
            confirmarcontrasenaField.setError("Las contraseñas no coinciden");
            errorTextView = confirmarcontrasenaTextView;
            isValid = false;
        }

        // If your form is inside a ScrollView, scroll to the first error message.
        if (!isValid) {
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.smoothScrollTo(0, errorTextView.getTop());
        }
        return isValid;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
}