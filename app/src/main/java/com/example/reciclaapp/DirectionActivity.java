package com.example.reciclaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class DirectionActivity extends AppCompatActivity {

    // Declare references to form fields and error message TextView
    EditText calleField;
    EditText numeroField;
    EditText coloniaField;
    EditText municipioField;
    EditText codigoPostalField;
    EditText telefonoField;

    // Get references to TextView elements for required fields
    TextView calleTextView;
    TextView numeroTextView;
    TextView coloniaTextView;
    TextView municipioTextView;
    TextView codigoPostalTextView;
    TextView telefonoTextView;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        // Find the Toolbar by its ID and set the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title for the app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Enable the back button (up navigation)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize the references to form fields and error message
        calleField = findViewById(R.id.Calle);
        numeroField = findViewById(R.id.numeroCasa);
        coloniaField = findViewById(R.id.colonia);
        municipioField = findViewById(R.id.municipio);
        codigoPostalField = findViewById(R.id.codigoPostal);
        telefonoField = findViewById(R.id.telefono);

        // Get references to TextView elements for required fields
        calleTextView = findViewById(R.id.textView4);
        numeroTextView = findViewById(R.id.textView3);
        coloniaTextView = findViewById(R.id.textView2);
        municipioTextView = findViewById(R.id.textView1);
        codigoPostalTextView = findViewById(R.id.textView5);
        telefonoTextView = findViewById(R.id.textView6);
    }

    public void localizar_en_mapa(View view) {
        // Check if the form is valid
        if (isFormValid()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent intent = new Intent(this, OrdenHorarioActivity.class);
        startActivity(intent);
        return true;
    }

    private boolean isFormValid() {
        boolean isValid = true;

        if (isEmpty(telefonoField)) {
            telefonoField.setError("Este campo es obligatorio");
            errorTextView = telefonoTextView;
            isValid = false;
        }

        if (isEmpty(codigoPostalField)) {
            codigoPostalField.setError("Este campo es obligatorio");
            errorTextView = codigoPostalTextView;
            isValid = false;
        }

        if (isEmpty(municipioField)) {
            municipioField.setError("Este campo es obligatorio");
            errorTextView = municipioTextView;
            isValid = false;
        }

        if (isEmpty(coloniaField)) {
            coloniaField.setError("Este campo es obligatorio");
            errorTextView = coloniaTextView;
            isValid = false;
        }

        if (isEmpty(numeroField)) {
            numeroField.setError("Este campo es obligatorio");
            errorTextView = numeroTextView;
            isValid = false;
        }

        if (isEmpty(calleField)) {
            calleField.setError("Este campo es obligatorio");
            errorTextView = calleTextView;
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
