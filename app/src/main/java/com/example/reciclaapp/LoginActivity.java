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
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText correoField;
    EditText contrasenaField;

    TextView correoTextView;
    TextView contrasenaTextView;
    TextView errorTextView;

    String correoText;
    String contrasenaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correoField = findViewById(R.id.mail);
        contrasenaField = findViewById(R.id.password);

        correoTextView = findViewById(R.id.textView4);
        contrasenaTextView = findViewById(R.id.textView5);

    }

    public void goToRegistro(View v){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void Continuar (View v){
        if (isFormValid()) {
            correoText = correoField.getText().toString();
            contrasenaText = contrasenaField.getText().toString();
            if (correoText.equals("useradmin@gmail.com") && contrasenaText.equals("useradmin123")){
                Intent intent = new Intent(this, NewsActivity.class);
                startActivity(intent);
            }
            else{
                final View backgroundView = new View(this);
                backgroundView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                backgroundView.setBackgroundColor(Color.argb(150, 0, 0, 0)); // Color semitransparente

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // Inflar el diseño personalizado
                View dialogView = getLayoutInflater().inflate(R.layout.contrasenaincorrrecta, null);

                // Configurar el diálogo
                builder.setView(dialogView);

                // Personalizar el diálogo
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                // Configurar un fondo semitransparente
                Window window = alertDialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.gravity = Gravity.CENTER;
                    window.setAttributes(params);
                }

                alertDialog.show();

                // Configurar acciones de los botones
                Button btnVolverIntentar = dialogView.findViewById(R.id.TryAgainButton);

                btnVolverIntentar.setOnClickListener(new View.OnClickListener() {
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
    }

    public void ContinuarFacebook (View v){
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
    }

    public void ContinuarGoogle (View v){
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
    }

    private boolean isFormValid() {
        boolean isValid = true;

        if (isEmpty(correoField)) {
            correoField.setError("Este campo es obligatorio");
            errorTextView = correoTextView;
            isValid = false;
        }

        if (isEmpty(contrasenaField)) {
            contrasenaField.setError("Este campo es obligatorio");
            errorTextView = contrasenaTextView;
            isValid = false;
        }
        return isValid;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
}